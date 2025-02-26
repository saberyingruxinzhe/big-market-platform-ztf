package com.ztf.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.ztf.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.ztf.domain.award.model.entity.TaskEntity;
import com.ztf.domain.award.model.entity.UserAwardRecordEntity;
import com.ztf.domain.award.repository.IAwardRepository;
import com.ztf.infrastructure.event.EventPublisher;
import com.ztf.infrastructure.persistent.dao.ITaskDao;
import com.ztf.infrastructure.persistent.dao.IUserAwardRecordDao;
import com.ztf.infrastructure.persistent.po.Task;
import com.ztf.infrastructure.persistent.po.UserAwardRecord;
import com.ztf.types.enums.ResponseCode;
import com.ztf.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Slf4j
@Component
public class AwardRepository implements IAwardRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        //第一个事务是用来处理提交抽奖奖品记录以及写入task表格中的
        //注意这里写入的task状态为create
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    //写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    //写入任务
                    taskDao.insert(task);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

        //这个事务是为了发送MQ消息，消息体为BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>
        //消息体内包含的信息为userId、awardId
        //然后会将对应的task的状态更改为completed
        //这样如果有MQ消息发送失败则会有task的状态不是completed

        //这里发送的MQ消息是什么作用？
        //todo
        try {
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(), task.getMessage());
            //更新数据库中的task表格
            //这里是更新对应userId、messageId状态为Completed
            taskDao.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }
}

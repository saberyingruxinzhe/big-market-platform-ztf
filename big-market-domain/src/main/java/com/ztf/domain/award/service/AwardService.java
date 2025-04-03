package com.ztf.domain.award.service;

import com.ztf.domain.award.event.SendAwardMessageEvent;
import com.ztf.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.ztf.domain.award.model.entity.DistributeAwardEntity;
import com.ztf.domain.award.model.entity.TaskEntity;
import com.ztf.domain.award.model.entity.UserAwardRecordEntity;
import com.ztf.domain.award.model.valobj.TaskStateVO;
import com.ztf.domain.award.repository.IAwardRepository;
import com.ztf.domain.award.service.distribute.IDistributeAward;
import com.ztf.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 防止异常的MQ发送失败
 * 以为MQ有概率发送失败，所以在保存数据到数据库的时候同时保存一份数据到task表格中，然后task转台设置为create
 * 然后新开一个事务用来发送MQ消息，并且将task中的状态更新为completed
 */
@Slf4j
@Service
public class AwardService implements IAwardService{

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;
    @Resource
    private Map<String, IDistributeAward> distributeAwardMap;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建MQ消息体
        //具体过程为先自己构建一个内部类SendAwardMessageEvent.SendAwardMessage
        //然后调用SendAwardMessageEvent的buildEventMessage将定义的SendAwardMessageEvent.SendAwardMessage作为参数放入
        //最后返回一个BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>
        //这里是将这个内部类作为data和之前的不一样，之前的是使用传入的sku作为data

        //和前面的不同在于：
        //前面继承BaseEvent的并没有自己实现内部类，并且不是使用内部类作为data，而是使用传入的sku作为data
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());


        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        //构建任务对象
        //其中包括用户id、MQ的交换机名称【定位交换机的位置】、MQ消息体id【定位在交换机中的位置】
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);

        // 构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        // 存储聚合对象 - 一个事务下，用户的中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        //奖品key
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if(null == awardKey){
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            return;
        }

        //奖品服务
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);

        if (null == distributeAward) {
            log.error("分发奖品，对应的服务不存在。awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }

        //发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }


}

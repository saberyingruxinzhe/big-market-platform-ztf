package com.ztf.domain.rebate.service;

import com.ztf.domain.rebate.event.SendRebateMessageEvent;
import com.ztf.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.ztf.domain.rebate.model.entity.BehaviorEntity;
import com.ztf.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.ztf.domain.rebate.model.entity.TaskEntity;
import com.ztf.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.ztf.domain.rebate.model.valobj.TaskStateVO;
import com.ztf.domain.rebate.repository.IBehaviorRebateRepository;
import com.ztf.types.common.Constants;
import com.ztf.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BehaviorRebateService implements IBehaviorRebateService{

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    //入参BehaviorEntity提供userId、BehaviorTypeVO以及业务id：outBusinessNo
    //这个业务id对于不同的行为来说是不同的，比如支付就是外部支付提供的id
    //对于签到来说就是签到的时间作为id
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        //1.查询返利配置
        //返利配置包括行为类型【是签到还是支付】、返利类型【是返利sku还是返利积分】、返利描述以及返利配置【返利多少积分，或者skuId】
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if(null == dailyBehaviorRebateVOS || dailyBehaviorRebateVOS.isEmpty()){
            return new ArrayList<>();
        }

        //2.构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            //拼装业务id
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            //这个BehaviorRebateOrderEntity提供的属性有：
            //用户id、订单id、行为类型、返利类型、返利配置以及业务id
            //需要区分订单id以及业务id，
            // 业务id是唯一的，是使用用户id、返利类型、
            // BehaviorEntity的业务id【如果行为类型为支付则是外部支付传入的业务id，如果是签到则是时间】拼接成的
            //订单id是
            //todo
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            //MQ消息对象
            //注意构建的是内部类
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

            //构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage
                    = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            // 组装任务对象
            //注意MQ消息体封装到task中，因为发送MQ消息和更新task是一个事务
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setState(TaskStateVO.create);

            //构建聚合对象
            //上面已经将构成聚合对象的所有部件得到
            //返利订单、MQ消息体、任务对象，然后就是同一个事务下进行保存
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behaviorEntity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .build();

            //所以为什么会获得多个聚合对象，而不是一个行为实体对象获得一个聚合对象
            //todo
            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }
        // 3. 存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);

        // 4. 返回订单ID集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }
}

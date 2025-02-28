package com.ztf.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ztf.domain.activity.model.entity.SkuRechargeEntity;
import com.ztf.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.ztf.domain.rebate.event.SendRebateMessageEvent;
import com.ztf.domain.rebate.model.valobj.RebateTypeVO;
import com.ztf.types.enums.ResponseCode;
import com.ztf.types.event.BaseEvent;
import com.ztf.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RebateMessageCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message){
        try {
            log.info("监听用户行为返利消息 topic: {} message: {}", topic, message);
            // 1. 转换消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
                    }.getType());
            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            //之前有一个BehaviorType作为行为类型的【是签到还是支付】
            //现在添加一个返利的类型，需要进行区分【返沪sku还是返利积分】
            if(!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())){
                log.info("监听用户行为返利消息 - 非sku奖励暂时不处理 topic: {} message: {}", topic, message);
                return;
            }
            //2.入账奖励 - 入账奖励是延迟发放的
            /**
             * 这里的思想
             * controller中的接口中的createOrder方法实现的是：
             * 通过行为实体对象来构建一个聚合对象，这个聚合对象主要包括有：
             * Task【包含有MQ消息体】以及返利订单对象
             * 所以需要即时实现的只有插入订单到数据库，而对于账户余额的修改是在这个listener中的createOrder进行的
             * 在这里对于总、日、月的余额进行修改
             */
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            //这里调用的是之前是实现的sku入账的功能
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }
}

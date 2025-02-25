package com.ztf.infrastructure.event;

import com.ztf.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 理一下这里的具体过程
     * 首先构建一个BaseEvent.EventMessage，然后向publish方法传入两个入参，
     * 一个是BaseEvent.EventMessage的topic【String，作为交换器的名字】，一个是BaseEvent.EventMessage本身
     * 将后者转换成json，才可以使用rabbitTemplate
     *
     */
    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage){
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic, messageJson);
            log.info("发送MQ消息 topic:{} message:{}", topic, messageJson);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }
    }
}

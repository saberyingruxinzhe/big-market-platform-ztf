package com.ztf.domain.activity.event;

import com.ztf.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ActivitySkuStockZeroMessageEvent extends BaseEvent<Long> {
    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    //这个方法使用构建消息体,这里传入的就是sku
    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(sku)
                .build();
    }

    //topic就是上面注解中的value值，这个value值是用来发送mq消息的，具体表现为
    //rabbitTemplate.convertAndSend(topic, messageJson);
    //这个方法的入参的含义为topic确定交换器的名称，messageJson就是由BaseEvent.EventMessage转换成的json数据
    @Override
    public String topic() {
        return topic;
    }
}

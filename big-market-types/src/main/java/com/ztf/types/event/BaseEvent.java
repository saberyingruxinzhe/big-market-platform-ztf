package com.ztf.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public abstract class BaseEvent<T> {
    //这个方法用来构建消息体，本节的消息就是用来扣减真实的数据库的
    public abstract EventMessage<T> buildEventMessage(T date);


    //topic就是实现类中注解中的value值，这个value值是用来发送mq消息的，具体表现为
    //rabbitTemplate.convertAndSend(topic, messageJson);
    //这个方法的入参的含义为topic确定交换器的名称，messageJson就是由BaseEvent.EventMessage转换成的json数据
    public abstract String topic();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventMessage<T> {
        private String id;
        private Date timestamp;
        private T data;
    }
}

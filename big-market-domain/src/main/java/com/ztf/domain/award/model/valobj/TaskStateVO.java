package com.ztf.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

//任务状态值对象
//发送状态对应不同的MQ处理，
//第一次M数据库库存更新和第一次task插入是一个事务，然后第一次的MQ发送和task的修改是一个事务
//第一次插入task设置状态为create，但是MQ如果发送成功，则task就会被修改为complete
//通过辨别那些task状态不是complete，就可以判断出有哪些MQ发送失败
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete", "发送完成"),
    fail("fail", "发送失败"),
    ;

    private final String code;
    private final String desc;

}

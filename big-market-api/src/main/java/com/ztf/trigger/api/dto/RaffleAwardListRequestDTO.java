package com.ztf.trigger.api.dto;

import lombok.Data;

@Data
public class RaffleAwardListRequestDTO {
    //用户id
    private String userId;
    //抽奖活动id
    //会新增使用活动id来查询到奖品列表的方法，因为后面redis需要添加过期时间，所以需要activityId来查询到活动过期时间
    //并且活动id以及策略id是一一对应的关系，所以活动id的扩展性更好
    private Long activityId;
}

package com.ztf.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//抽奖因子实体类，用来存放抽奖最初需要的信息
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {
    //用户id
    private String userId;
    //策略id
    private Long strategyId;
    //奖品id，奖品id是为了抽奖中过滤的，因为抽奖中过滤是已经获得了奖品id了
    private Integer awardId;
}

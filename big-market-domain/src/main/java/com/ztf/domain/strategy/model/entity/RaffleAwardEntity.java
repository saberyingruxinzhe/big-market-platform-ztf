package com.ztf.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//抽奖后最终返回的奖品实体类
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
    //策略id
    private Long strategyId;
    //奖品id
    private Integer awardId;
    //奖品对接标识，每一个标识都是一个对应的发奖策略
    private String awardKey;
    //奖品配置信息
    private String awardConfig;
    //奖品内容描述
    private String awardDesc;
}

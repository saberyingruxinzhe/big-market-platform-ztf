package com.ztf.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyConditionEntity {
    //用户id
    private String userId;
    //策略id
    private Integer strategyId;
}

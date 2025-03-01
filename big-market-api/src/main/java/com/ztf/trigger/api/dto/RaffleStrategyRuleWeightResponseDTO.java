package com.ztf.trigger.api.dto;

import lombok.Data;

import java.util.List;

//抽奖策略规则，权重配置，查询N次抽奖可解锁奖品范围，应答对象
//查询到信息包括：
//1、权重规则配置的需要抽奖多少次才可以解锁奖品范围
//2、当前活动中用户已经抽奖的次数
//3.用户当前可以抽到的奖品范围
@Data
public class RaffleStrategyRuleWeightResponseDTO {

    // 权重规则配置的抽奖次数，
    // 可能是2000，也可能是4000
    private Integer ruleWeightCount;
    // 用户在一个活动下完成的总抽奖次数
    private Integer userActivityAccountTotalUseCount;
    // 当前权重可抽奖范围
    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward {
        // 奖品ID
        private Integer awardId;
        // 奖品标题
        private String awardTitle;
    }

}

package com.ztf.domain.strategy.model.entity;

import lombok.Data;

//规则物料实体对象，是过滤类Filter的filter方法的入参
@Data
public class RuleMatterEntity {
    //用户id
    private String userId;

    //策略id
    private Long strategyId;

    //奖品id，如果规则类型为策略，则不需要奖品id
    //这里其实是为了后面抽奖中、后的过滤准备的，因为抽奖前显然还没获取到奖品id
    private Integer awardId;

    //抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】
    private String ruleModel;
}

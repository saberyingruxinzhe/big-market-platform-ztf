package com.ztf.domain.strategy.service.armory;

//这个接口用来实际生成随机数抽奖
public interface IStrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

}

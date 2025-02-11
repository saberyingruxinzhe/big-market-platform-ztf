package com.ztf.domain.strategy.service.armory;

//策略装配兵工厂接口
public interface IStrategyArmory {

    boolean assembleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);
}

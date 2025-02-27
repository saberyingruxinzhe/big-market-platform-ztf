package com.ztf.domain.strategy.service.armory;

import com.ztf.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

//策略装配兵工厂接口
public interface IStrategyArmory {

    boolean assembleLotteryStrategy(Long strategyId);

    boolean assembleLotteryStrategyByActivityId(Long activityId);
}

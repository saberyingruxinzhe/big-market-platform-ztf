package com.ztf.domain.strategy.service;

import com.ztf.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IRaffleAward {
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}

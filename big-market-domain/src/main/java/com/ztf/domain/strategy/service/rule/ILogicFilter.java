package com.ztf.domain.strategy.service.rule;

import com.ztf.domain.strategy.model.entity.RuleActionEntity;
import com.ztf.domain.strategy.model.entity.RuleMatterEntity;


public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter (RuleMatterEntity ruleMatterEntity);
}

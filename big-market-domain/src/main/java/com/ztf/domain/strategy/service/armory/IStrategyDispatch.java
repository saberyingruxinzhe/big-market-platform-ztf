package com.ztf.domain.strategy.service.armory;

//这个接口用来实际生成随机数抽奖
public interface IStrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    //
    Integer getRandomAwardId(String key);

    //根据策略id和奖品id确扣减奖品缓存库存
    Boolean subtractionAwardStock(Long strategyId, Integer awardId);

}

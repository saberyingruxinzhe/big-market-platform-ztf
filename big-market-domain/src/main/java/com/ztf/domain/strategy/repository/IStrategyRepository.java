package com.ztf.domain.strategy.repository;


import com.ztf.domain.strategy.model.entity.StrategyAwardEntity;
import com.ztf.domain.strategy.model.entity.StrategyEntity;
import com.ztf.domain.strategy.model.entity.StrategyRuleEntity;
import com.ztf.domain.strategy.model.valobj.RuleTreeVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    int getRateRange(String strategyId);

    Integer getStrategyAwardAssemble(String strategyId, Integer rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    //这个方法是为了前置过滤准备的，因为前置过滤没有奖品id
    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    //根据规则树id，获取到树结构信息（这里传入的树id形式为rule_lock,rule_luck_award）
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    //缓存奖品库存
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    //decr方式扣减库存,注意这里扣减的是redis中缓存的库存
    Boolean subtractionAwardStock(String cacheKey);


    //写入奖品库存消费队列
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    //获取奖品库消费队列中的StrategyAwardStockKeyVO对象，
    //这个对象是在扣减redis库存成功之后加入到奖品库存消费队列中的
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    //更新奖品库存消耗，这里是更新正真的数据库的库存
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    //根据策略id + 奖品id的唯一值组合获取到奖品信息
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);
}

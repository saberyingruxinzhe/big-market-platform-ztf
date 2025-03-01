package com.ztf.domain.strategy.service.rule.chain.impl;

import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.armory.IStrategyDispatch;
import com.ztf.domain.strategy.service.armory.StrategyArmoryDispatch;
import com.ztf.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Resource
    private IStrategyDispatch strategyDispatch;


    @Resource
    private StrategyArmoryDispatch strategyArmoryDispatch;


    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {

        String ruleValue = repository.queryStrategyRuleValue(
                strategyId, ruleModel()
        );

        //1.根据用户消耗的积分制，对比选取哪一种权重
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if(null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            //放行
            return next().logic(userId, strategyId);
        }

        //2.将map的key转换为list,并进行排序方便对比
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        //3。开始逐个对比哪一个权重合适
        Integer userScore = repository.queryActivityAccountTotalUseCount(userId, strategyId);
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        //4. 权重抽奖
        if(null != nextValue) {
            //拦截
            //这里就直接计算出来抽到的奖品了，不用走默认的抽奖责任链节点了，
            //因为默认的责任链的logic使用的repository.getRandomAwardId是没有权重作为入参的
            //这个方法原本应该在AbstractRaffleStrategy中执行的，现在放到这里了
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return DefaultChainFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .logicModel(ruleModel())
                    .build();
        }

        //这里放行是因为用户积分还未达到拦截的标准
        // 5. 过滤其他责任链
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    //这个方法获取到的map中键值对的形式如下：key为4000，value为4000:102,103,104,105
    //为什么要保持这个value，因为存放到redis中拼接的就是这个形式的字符串
    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroup = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroup) {
            if(ruleValueKey == null || ruleValueKey.isEmpty()){
                return ruleValueMap;
            }
            String[] parts = ruleValueKey.split(Constants.COLON);
            if(parts.length != 2){
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }
}

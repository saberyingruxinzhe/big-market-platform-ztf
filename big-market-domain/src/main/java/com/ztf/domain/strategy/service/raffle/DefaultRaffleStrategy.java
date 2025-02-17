package com.ztf.domain.strategy.service.raffle;

import com.ztf.domain.strategy.model.valobj.RuleTreeVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.AbstractRaffleStrategy;
import com.ztf.domain.strategy.service.armory.IStrategyDispatch;
import com.ztf.domain.strategy.service.rule.chain.ILogicChain;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.ztf.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


//是这个类添加@Service注解，而不是AbstractRaffleStrategy类
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if(null == strategyAwardRuleModelVO){
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }

        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if(null == ruleTreeVO){
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }


    //已经不需要前置过滤的方法了，这里的逻辑都由责任链表实现了

//    @Override
//    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {
//        if(logics == null || logics.length == 0){
//            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
//                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
//                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
//                    .build();
//        }
//
//        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
//
//        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;
//        //为什么这里使用for循环，如果其中一个元素拦截的话直接就返回，那么for循环还有什么意义呢。
//        for(String ruleModel: logics){
//            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);
//            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
//            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
//            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
//            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
//            ruleMatterEntity.setRuleModel(ruleModel);
//
//            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
//            // 非放行结果则顺序过滤
//            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
//            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
//        }
//        return ruleActionEntity;
//
//    }
}

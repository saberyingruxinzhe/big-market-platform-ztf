package com.ztf.domain.strategy.service.raffle;

import com.ztf.domain.strategy.model.entity.RaffleFactorEntity;
import com.ztf.domain.strategy.model.entity.RuleActionEntity;
import com.ztf.domain.strategy.model.entity.RuleMatterEntity;
import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.AbstractRaffleStrategy;
import com.ztf.domain.strategy.service.armory.IStrategyDispatch;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.domain.strategy.service.rule.filter.ILogicFilter;
import com.ztf.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//是这个类添加@Service注解，而不是AbstractRaffleStrategy类
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    private DefaultLogicFactory logicFactory;
    
    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        super(repository, strategyDispatch, defaultChainFactory);
    }

    //已经不需要前置过滤的方法了，这里的逻辑都由责任链表实现了

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {
        if(logics == null || logics.length == 0){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;
        //为什么这里使用for循环，如果其中一个元素拦截的话直接就返回，那么for循环还有什么意义呢。
        for(String ruleModel: logics){
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);

            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            // 非放行结果则顺序过滤
            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }
        return ruleActionEntity;

    }
}

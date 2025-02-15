package com.ztf.domain.strategy.service;

import com.ztf.domain.strategy.model.entity.RaffleAwardEntity;
import com.ztf.domain.strategy.model.entity.RaffleFactorEntity;
import com.ztf.domain.strategy.model.entity.RuleActionEntity;
import com.ztf.domain.strategy.model.entity.StrategyEntity;
import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.armory.IStrategyDispatch;
import com.ztf.domain.strategy.service.rule.chain.ILogicChain;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.ztf.types.enums.ResponseCode;
import com.ztf.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository repository;

    protected IStrategyDispatch strategyDispatch;

    protected DefaultChainFactory defaultChainFactory;

    //上面引入没有使用@Resource，这里要使用构造器
    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;

        //构造这个工厂类不在抽奖方法中进行
        this.defaultChainFactory = defaultChainFactory;
    }


    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1.参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (StringUtils.isBlank(userId) || null == strategyId) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2.获取抽奖责任链，前置的抽奖过滤
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        //3.抽奖前过滤,获取到奖品id
        Integer awardId = logicChain.logic(userId, strategyId);

        //5.查询着抽到的奖品是否是带有lock的即看是否需要进行中置过滤
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        //6.进行抽奖中过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(
                RaffleFactorEntity.builder()
                        .userId(userId)
                        .strategyId(strategyId)
                        .awardId(awardId)
                        .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList()
        );
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    //这里的入参logics也就是ruleModels
    //这个方法用来获取到过滤后的RuleActionEntity对象

    //已经不需要前置过滤的方法了，这里的逻辑都由责任链表实现了
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    //这个方法针对抽奖中过滤
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    //这个方法针对抽奖后过滤
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleCenterLogic(RaffleFactorEntity build, String... logics);
}
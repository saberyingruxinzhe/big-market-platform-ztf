package com.ztf.domain.strategy.service;

import com.ztf.domain.strategy.model.entity.RaffleAwardEntity;
import com.ztf.domain.strategy.model.entity.RaffleFactorEntity;
import com.ztf.domain.strategy.model.entity.RuleActionEntity;
import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.armory.IStrategyDispatch;
import com.ztf.domain.strategy.service.rule.chain.ILogicChain;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.ztf.types.enums.ResponseCode;
import com.ztf.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    protected IStrategyRepository repository;

    protected IStrategyDispatch strategyDispatch;

    protected DefaultChainFactory defaultChainFactory;

    protected DefaultTreeFactory defaultTreeFactory;

    //上面引入没有使用@Resource，这里要使用构造器
    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;

        //构造这个工厂类不在抽奖方法中进行
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }


    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1.参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (StringUtils.isBlank(userId) || null == strategyId) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2.责任链抽奖计算【这步拿到的是初步的奖品id，之后会将这个奖品id进一步通过规则树处理】
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        //如果是黑名单或者权重规则就直接返回，不用进行下面的规则树过滤
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())){
            return RaffleAwardEntity.builder()
                    .awardId(chainStrategyAwardVO.getAwardId())
                    .build();
        }

        //3.规则树抽奖过滤
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        //4.返回结果
        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAwardVO.getAwardId())
                .awardConfig(treeStrategyAwardVO.getAwardRuleValue())
                .build();



    }

    //这里的入参logics也就是ruleModels
    //这个方法用来获取到过滤后的RuleActionEntity对象

    //已经不需要前置过滤的方法了，这里的逻辑都由责任链表实现了
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    //这个方法针对抽奖中过滤
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    //这个方法针对抽奖后过滤
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleCenterLogic(RaffleFactorEntity build, String... logics);


    /**
     * 下面新添加两个方法区别于上面的处理前置、中置过滤的方法
     * 添加处理chain、tree过滤的方法
     */

    //处理责任链
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    //处理规则树
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}
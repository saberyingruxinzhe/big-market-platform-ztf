package com.ztf.domain.strategy.service;

import com.ztf.domain.strategy.model.entity.RaffleAwardEntity;
import com.ztf.domain.strategy.model.entity.RaffleFactorEntity;
import com.ztf.domain.strategy.model.entity.RuleActionEntity;
import com.ztf.domain.strategy.model.entity.StrategyAwardEntity;
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


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 责任链抽奖计算【这步拿到的是初步的抽奖ID，之后需要根据ID处理抽奖】注意；黑名单、权重等非默认抽奖的直接返回抽奖结果
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        if (!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            // TODO awardConfig 暂时为空。黑名单指定积分奖品，后续需要在库表中配置上对应的1积分值，并获取到。
            return buildRaffleAwardEntity(strategyId, chainStrategyAwardVO.getAwardId(), null);
        }

        // 3. 规则树抽奖过滤【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        // 4. 返回抽奖结果
        return buildRaffleAwardEntity(strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());
    }

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig) {
        StrategyAwardEntity strategyAward = repository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardTitle(strategyAward.getAwardTitle())
                .awardConfig(awardConfig)
                .sort(strategyAward.getSort())
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
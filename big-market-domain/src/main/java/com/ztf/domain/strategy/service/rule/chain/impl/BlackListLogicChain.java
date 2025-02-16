package com.ztf.domain.strategy.service.rule.chain.impl;

import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.ztf.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.ztf.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        //这里直接获取ruleValues，不需要从获取StrategyEntity开始，获取StrategyEntity是工厂类做的事

        //查询规则值配置
        //取出的ruleValue形式为：100:user001,user002,user003，这里第一个100为这些黑名单用户可以获取到的奖品id
        String ruleValue = repository.queryStrategyRuleValue(
                strategyId,
                ruleModel()
        );
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        //对比当前用户是否是黑名单用户
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for(String blackId : userBlackIds) {
            if(userId.equals(blackId)){
                //如果是黑名单用户就拦截
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }

        // 过滤其他责任链
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    //这个方法其实有点多此一举？只有在这个类中使用，或者是为了后续的扩展功能的考虑
    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }
}

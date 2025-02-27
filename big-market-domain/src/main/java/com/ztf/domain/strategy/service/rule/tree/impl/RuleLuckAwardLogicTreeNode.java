package com.ztf.domain.strategy.service.rule.tree.impl;


import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.ztf.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.ztf.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    //这里传入的ruleValue是兜底奖品的积分获取范围，比如101:1,100，意思是兜底奖品id为101，并且为1到100的积分
    //需要注意的是这里获取到的ruleValue不是strategy_rule表格中的rule_value，而是rule_tree_node中的rule_value
    //不同之处在于，前一个形式为：1,100，后一个用的是101:1,100
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId, ruleValue);

        String[] split = ruleValue.split(Constants.COLON);
        if(split.length == 0){
            log.error("规则过滤-兜底奖品，兜底奖品未配置告警 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
            throw new RuntimeException("兜底奖品未配置 " + ruleValue);
        }
        //兜底奖励配置
        Integer luckAwardId = Integer.valueOf(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";
        //返回兜底奖品
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} awardRuleValue:{}", userId, strategyId, luckAwardId, awardRuleValue);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(luckAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();

    }
}

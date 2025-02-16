package com.ztf.domain.strategy.service.rule.tree.factory;

import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.model.valobj.RuleTreeVO;
import com.ztf.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.ztf.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.ztf.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTreeFactory {
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup){
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO){
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }


    //这个类就是用来平替RuleActionEntity的？？？
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity{
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardData strategyAwardData;
    }

    //这里存放的ruleValue值是指？？？
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardData{
        private Integer awardId;
        private String awardRuleValue;
    }

}

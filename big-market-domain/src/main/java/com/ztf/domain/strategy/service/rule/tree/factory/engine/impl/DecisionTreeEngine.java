package com.ztf.domain.strategy.service.rule.tree.factory.engine.impl;

import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ztf.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.ztf.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.ztf.domain.strategy.model.valobj.RuleTreeVO;
import com.ztf.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.ztf.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.ztf.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    /**
     * 这里分析一下整个规则树的装配过程
     * 1、首先通过构造器的传入的ruleTreeVO获取到根节点的rule_value  以及  整个规则树节点的rule_value和树节点RuleTreeNodeVO的映射map：treeNodeMap
     * 2、然后可以通过上述的treeNodeMap获取到相应的根节点
     * 3、使用while循环来构建整个规则树，并且不止是构建，还要在过程中执行规则过滤
     * 4、然后通过构造器传入的规则树节点的rule_value和ILogicTreeNode的映射map，获取到真正的执行过滤策略的树节点，并执行过滤策略logic方法
     * 5、通过执行完之后返回的DefaultTreeFactory.TreeActionEntity可以得到两个东西，
     * 一个是含有下一个节点去向信息的RuleLogicCheckTypeVO，一个是需要返回的DefaultTreeFactory.StrategyAwardData对象
     * 但是这个需要返回的对象需要多次循环之后才能返回
     */

    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        //获取基础信息
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        //获取起始节点（根节点记录了第一个要执行的规则）
        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);
        while (null != nextNode) {
            //获取决策节点，即真正执行过滤的ILogicTreeNode
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNode.getRuleKey());

            //决策节点计算,即执行树节点的logic方法完成过滤
            //获取到RuleLogicCheckTypeVO的目的是为了判断之后要往那个方向的树节点去
            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            //奖品对象也在一轮一轮刷新
            strategyAwardVO = logicEntity.getStrategyAwardVO();

            //获取到下一个节点
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(nextNode);

        }
        return strategyAwardVO;
    }

    public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        //这里分析一下两个入参，一个是提供这次的抽奖是被接管了还是被放行了，这个决定了向哪一个树节点去
        //另一个入参即为RuleTreeNodeVO中的属性treeNodeLineVOList，这个里面包含了当前节点可以去的方向
        //这个树规则中，可以有两个所去的方向
        if (null == treeNodeLineVOList || treeNodeLineVOList.isEmpty()) {
            return null;
        }
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");
    }

    //通过code确定指向的树节点
    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                //这里就是对code进行判断，传进来的matterValue是从DefaultTreeFactory.TreeActionEntity中获取到的code
                //这个code和连线中RuleTreeNodeLineVO的code做对比，如果对上了，就会确定指向的树节点
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}

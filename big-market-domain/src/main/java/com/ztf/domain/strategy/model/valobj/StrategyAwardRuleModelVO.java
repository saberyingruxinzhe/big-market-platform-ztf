package com.ztf.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    //这个属性就是对应strategy_award表格中的ruleModels属性
    //比如策略100001，奖品109对应的这一属性为rule_lock,rule_luck_award
    private String ruleModels;


    /**
     * 为什么下面的代码不需要了
     * 1、因为前置的过滤已经不需要使用这个vo了，因为在chain的工厂类中，通过直接获取到StrategyEntity已经解决了这个问题
     * 2、中置和后置过滤合并成tree，因为工厂类需要的入参为TreeNodeVO，所以这里需要将ruleModel转化为TreeNodeVO类型，
     * 在repository中添加的queryRuleTreeVOByTreeId方法已经处理了相关的功能
     * 所以不需要下面的代码了
     */
    //这一方法用来将上面的ruleModels拆分，看是否有需要进行中置过滤的rule_model
    //为什么最后返回的是一个list，是为了扩展性的考虑，因为中置过滤之后的需求中不一定只有一个rule_lock这个rule_model
//    public String[] raffleCenterRuleModelList(){
//        List<String> ruleModelList = new ArrayList<>();
//        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
//        for (String ruleModelValue : ruleModelValues) {
//            if(DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)){
//                ruleModelList.add(ruleModelValue);
//            }
//        }
//        return ruleModelList.toArray(new String[0]);
//    }
//
//    public String[] raffleAfterRuleModelList(){
//        List<String> ruleModelList = new ArrayList<>();
//        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
//        for (String ruleModelValue : ruleModelValues) {
//            if(DefaultLogicFactory.LogicModel.isAfter(ruleModelValue)){
//                ruleModelList.add(ruleModelValue);
//            }
//        }
//        return ruleModelList.toArray(new String[0]);
//    }

}

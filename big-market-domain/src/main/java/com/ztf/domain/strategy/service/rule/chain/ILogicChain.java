package com.ztf.domain.strategy.service.rule.chain;

public interface ILogicChain extends ILogicChainArmory{
    //这个方法中即是过滤的逻辑，返回的Integer即为奖品id
    //这里logic实现的就是之前Filter中filter方法中的逻辑，只是之前filter的入参为RuleMatterEntity对象，现在变成了userId以及策略id
    //之前的RuleMatterEntity对象包含userId、strategyId、awardId以及ruleModel，后面两个前置抽奖中不需要，所以不用这个类作为入参
    //而且现在返回的也不是RuleActionEntity，而是直接是奖品id
    Integer logic(String userId, Long strategyId);
}

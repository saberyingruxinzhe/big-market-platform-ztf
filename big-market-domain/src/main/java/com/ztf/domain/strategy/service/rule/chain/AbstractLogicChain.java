package com.ztf.domain.strategy.service.rule.chain;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractLogicChain implements ILogicChain{
    private ILogicChain next;



    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    //这个方法的作用为：
    //返回对应的责任链节点的关键字，即工厂map中的key
    protected abstract String ruleModel();
}

package com.ztf.domain.strategy.service.rule.chain.factory;

import com.ztf.domain.strategy.model.entity.StrategyEntity;
import com.ztf.domain.strategy.repository.IStrategyRepository;
import com.ztf.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

//注意是在工厂类中添加@Service注解，而不是在ILogicChain的实现类中添加注解
//为什么。只因为需要@Component引入三个责任链节点吗
@Service
public class DefaultChainFactory {
    private static final Logger log = LoggerFactory.getLogger(DefaultChainFactory.class);
    private final Map<String, ILogicChain> logicChainGroup;
    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository){
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.ruleModels();

        //如果这个ruleModels为空，就直接返回默认的责任链节点
        //这也就是为什么strategy表格中ruleModels属性栏中并没有放上所有strategyId对应的ruleModel
        if(null == ruleModels || 0 == ruleModels.length){
            return logicChainGroup.get("default");
        }

        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        //否则按照顺序装配责任链节点
        for(int i = 1;i < ruleModels.length;i++){
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            //这就是为什么appendNext会返回一个ILogicChain对象
            current = current.appendNext(nextChain);
        }

        //责任链最后再添加上默认的责任链节点
        current.appendNext(logicChainGroup.get("default"));
        return logicChain;
    }

    //上一节中是直接返回奖品id，这里将返回的类型进行精细化、模范化处理
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /**  */
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }
}

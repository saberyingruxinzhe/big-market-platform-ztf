package com.ztf.domain.strategy.model.entity;

import com.ztf.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {
    //抽奖策略ID
    private Long strategyId;
    //抽奖策略描述
    private String strategyDesc;
    //抽奖规则模型
    private String ruleModels;

    //获取到ruleModel的数组，比如rule_blacklist以及rule_weight
    public String[] ruleModels(){
        if(StringUtils.isBlank(ruleModels)){
            return null;
        }
        return ruleModels.split(Constants.SPLIT);
    }

    //如果rule_model是rule_weight才会返回，不然返回null
    public String getRuleWeight(){
        String[] ruleModels = this.ruleModels();
        if (null == ruleModels) return null;
        for(String ruleModel : ruleModels){
            if("rule_weight".equals(ruleModel)){
                return ruleModel;
            }
        }
        return null;
    }
}

package com.ztf.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {
    //规则树id
    private Integer treeId;

    //规则key节点from,这个就是哪些rule_value
    private String ruleNodeFrom;

    //规则key节点to，rule_value
    private String ruleNodeTo;

    //限定类型：1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围]
    private RuleLimitTypeVO ruleLimitType;

    //限定值（到下一个节点），即拦截还是不拦截
    //下面的解释表明，所有的情况都在表格中列出，而每一种情况都对应一个限定值
    //比如rule_lock到rule_stock就是ALLOW，因为从锁定到库存表明用户积分符合条件，放行
    //rule_lock到rule_luck_award就是TAKE_OVER，因为从锁定到兜底表明用户积分没有符合条件，拦截
    //rule_stock到rule_luck_award为TAKE_OVER，因为从库存到兜底表明库存不足，拦截
    /**
     * 但是需要注意的是这里的类型为RuleLogicCheckTypeVO，并不是String
     */
    private RuleLogicCheckTypeVO ruleLimitValue;

    //以上属性的解释：
    //在数据库中有个rule_tree_node_line表格，这个表格中列举了所有可能的from到to的情况
    //在本项目中，只有三种情况，rule_lock到rule_stock，rule_lock到rule_luck_award，rule_stock到rule_luck_award
}

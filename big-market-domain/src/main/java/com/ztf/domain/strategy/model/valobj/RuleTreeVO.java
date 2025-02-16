package com.ztf.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

//规则书vo值对象
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {
    //树id
    private Integer treeId;
    //规则树名称
    private String treeName;
    //规则树描述
    private String treeDesc;
    //规则树的根节点，这里是根节点对应的ruleModel，就是下面的Map的key值
    private String treeRootRuleNode;
    //规则节点，这里的key为rule_lock、rule_luck_award、rule_stock等ruleModel，value则是规则树节点值对象
    private Map<String, RuleTreeNodeVO> treeNodeMap;
}

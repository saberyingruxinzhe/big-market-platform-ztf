package com.ztf.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//规则树节点值对象
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeVO {
    //规则树id
    private Integer treeId;
    //规则key
    private String ruleKey;
    //规则描述
    private String ruleDesc;
    //规则比值，比如
    private String ruleValue;
    //规则连线list
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}

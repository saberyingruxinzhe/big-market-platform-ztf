package com.ztf.infrastructure.persistent.dao;

import com.ztf.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

}

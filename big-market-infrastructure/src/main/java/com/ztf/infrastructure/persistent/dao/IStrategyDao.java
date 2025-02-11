package com.ztf.infrastructure.persistent.dao;

import com.ztf.infrastructure.persistent.po.Strategy;
import com.ztf.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}

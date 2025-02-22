package com.ztf.infrastructure.persistent.dao;

import com.ztf.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

//抽奖活动表
@Mapper
public interface IRaffleActivityDao {

    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

}

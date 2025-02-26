package com.ztf.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.ztf.infrastructure.persistent.po.UserRaffleOrder;
import org.apache.ibatis.annotations.Mapper;

//用户抽奖订单表
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDao {
    void insert(UserRaffleOrder userRaffleOrder);

    @DBRouter
    UserRaffleOrder queryNoUsedRaffleOrder(UserRaffleOrder userRaffleOrderReq);
}

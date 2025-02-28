package com.ztf.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.ztf.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//抽奖活动账户表-月次数
public interface IRaffleActivityAccountMonthDao {
    @DBRouter
    RaffleActivityAccountMonth queryActivityAccountMonthByUserId(RaffleActivityAccountMonth raffleActivityAccountMonthReq);

    int updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountMonth raffleActivityAccountMonth);

    void insertActivityAccountMonth(RaffleActivityAccountMonth raffleActivityAccountMonth);

    void addAccountQuota(RaffleActivityAccountMonth raffleActivityAccountMonth);

}

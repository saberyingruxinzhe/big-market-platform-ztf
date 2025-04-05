package com.ztf.domain.credit.repository;

import com.ztf.domain.credit.model.aggregate.TradeAggregate;

//用户积分仓储
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

}

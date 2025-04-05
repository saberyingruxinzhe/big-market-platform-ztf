package com.ztf.domain.credit.service;

import com.ztf.domain.credit.model.entity.TradeEntity;

public interface ICreditAdjustService {
    //创建增加积分额度订单
    String createOrder(TradeEntity tradeEntity);
}

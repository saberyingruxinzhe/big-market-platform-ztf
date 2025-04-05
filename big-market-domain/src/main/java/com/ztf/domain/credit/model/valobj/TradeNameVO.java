package com.ztf.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

//分为两类：行为（签到、支付），抽奖
@Getter
@AllArgsConstructor
public enum TradeNameVO {

    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;

    private final String name;

}

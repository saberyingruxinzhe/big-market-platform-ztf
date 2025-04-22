package com.ztf.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

//交易类型，是需要支付的还是不需要支付的
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {

    credit_pay_trade("credit_pay_trade","积分兑换，需要支付类交易"),
    rebate_no_pay_trade("rebate_no_pay_trade", "返利奖品，不需要支付类交易"),
    ;

    private final String code;
    private final String desc;

}

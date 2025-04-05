package com.ztf.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

//增加积分或者扣减积分
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "逆向交易，- 积分"),

    ;

    private final String code;
    private final String info;

}

package com.ztf.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

//用户账户状态，如果是冻结状态就会列为黑名单用户
@Getter
@AllArgsConstructor
public enum AccountStatusVO {

    open("open", "开启"),
    close("close", "冻结"),
    ;

    private final String code;
    private final String desc;

}

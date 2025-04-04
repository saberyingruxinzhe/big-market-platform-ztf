package com.ztf.infrastructure.persistent.dao;

import com.ztf.infrastructure.persistent.po.UserCreditAccount;
import org.apache.ibatis.annotations.Mapper;

//用户积分账户
@Mapper
public interface IUserCreditAccountDao {
    void insert(UserCreditAccount userCreditAccountReq);

    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);

}
package com.ztf.domain.award.service;

import com.ztf.domain.award.model.entity.DistributeAwardEntity;
import com.ztf.domain.award.model.entity.UserAwardRecordEntity;

public interface IAwardService {
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    //配置发货奖品
    void distributeAward(DistributeAwardEntity distributeAwardEntity);
}

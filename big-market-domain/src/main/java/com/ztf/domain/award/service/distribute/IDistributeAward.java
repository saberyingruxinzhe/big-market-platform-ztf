package com.ztf.domain.award.service.distribute;

import com.ztf.domain.award.model.entity.DistributeAwardEntity;

public interface IDistributeAward {
    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}

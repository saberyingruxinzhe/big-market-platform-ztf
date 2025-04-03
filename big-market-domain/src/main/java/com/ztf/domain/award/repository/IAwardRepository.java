package com.ztf.domain.award.repository;

import com.ztf.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.ztf.domain.award.model.aggregate.UserAwardRecordAggregate;

public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    String queryAwardConfig(Integer awardId);

    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    String queryAwardKey(Integer awardId);
}

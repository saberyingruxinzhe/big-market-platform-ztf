package com.ztf.domain.activity.service.quota.rule;

import com.ztf.domain.activity.model.entity.ActivityCountEntity;
import com.ztf.domain.activity.model.entity.ActivityEntity;
import com.ztf.domain.activity.model.entity.ActivitySkuEntity;

public interface IActionChain extends IActionChainArmory {
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}

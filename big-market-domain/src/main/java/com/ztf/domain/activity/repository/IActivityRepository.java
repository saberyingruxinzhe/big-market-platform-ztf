package com.ztf.domain.activity.repository;

import com.ztf.domain.activity.model.entity.ActivityCountEntity;
import com.ztf.domain.activity.model.entity.ActivityEntity;
import com.ztf.domain.activity.model.entity.ActivitySkuEntity;

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);
}

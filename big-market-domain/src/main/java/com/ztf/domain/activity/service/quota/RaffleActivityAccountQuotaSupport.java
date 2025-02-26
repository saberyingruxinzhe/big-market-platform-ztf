package com.ztf.domain.activity.service.quota;

import com.ztf.domain.activity.model.entity.ActivityCountEntity;
import com.ztf.domain.activity.model.entity.ActivityEntity;
import com.ztf.domain.activity.model.entity.ActivitySkuEntity;
import com.ztf.domain.activity.repository.IActivityRepository;
import com.ztf.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

public class RaffleActivityAccountQuotaSupport {
    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository activityRepository;

    //这里是通过构造器诸如repository以及factory，之后继承这个类的都可以通过super来继承
    public RaffleActivityAccountQuotaSupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}

package com.ztf.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.ztf.domain.activity.model.entity.*;
import com.ztf.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractRaffleActivity implements IRaffleOrder{
    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        //1.通过购物车对象获取到sku实体类
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        //2.查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //3.查询次数信息
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }
}

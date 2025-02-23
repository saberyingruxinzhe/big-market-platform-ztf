package com.ztf.domain.activity.service;

import com.ztf.domain.activity.model.entity.ActivityOrderEntity;
import com.ztf.domain.activity.model.entity.ActivityShopCartEntity;

public interface IRaffleOrder {
    //以sku创建抽奖活动订单，获得参与抽奖资格
    //入参为购物车实体类
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}

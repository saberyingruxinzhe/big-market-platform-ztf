package com.ztf.domain.rebate.service;

import com.ztf.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

public interface IBehaviorRebateService {
    /**
     * 创建行为动作的入账订单
     *
     * @param behaviorEntity 行为实体对象
     * @return 订单ID
     * 为什么这里返回的是一个关于订单id的list，根据一个行为进行创建订单不应该是只有一个订单吗
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);
}

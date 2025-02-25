package com.ztf.domain.activity.service.armory;

import java.util.Date;

//活动调度【扣减库存】
public interface IActivityDispatch {
    //根据策略ID和奖品ID，扣减奖品缓存库存
    //为什么这里是策略id和奖品id
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}

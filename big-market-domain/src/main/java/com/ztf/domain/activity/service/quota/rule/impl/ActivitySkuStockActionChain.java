package com.ztf.domain.activity.service.quota.rule.impl;

import com.ztf.domain.activity.model.entity.ActivityCountEntity;
import com.ztf.domain.activity.model.entity.ActivityEntity;
import com.ztf.domain.activity.model.entity.ActivitySkuEntity;
import com.ztf.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.ztf.domain.activity.repository.IActivityRepository;
import com.ztf.domain.activity.service.armory.IActivityDispatch;
import com.ztf.domain.activity.service.quota.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        //扣减库存
        //这里的逻辑为，首先尝试扣减redis中的库存，
        //如果redis中的库存已经小于等于0就直接发送mq消息进行延迟消费更新

        //为什么这里需要传入sku：
        //1、需要使用sku来构建redis的key
        //2、mq消息中封装的主要信息就是sku
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        //如果status为true就表示扣减成功
        //扣减成功进行操作就是
        if(status){
            log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】成功。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

            //写入延迟队列，延迟消费更新库存记录
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());
        }
        return true;
    }
}

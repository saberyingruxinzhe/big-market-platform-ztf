package com.ztf.domain.activity.service;

import com.ztf.domain.activity.model.aggregate.CreateOrderAggregate;
import com.ztf.domain.activity.model.entity.*;
import com.ztf.domain.activity.repository.IActivityRepository;
import com.ztf.domain.activity.service.rule.IActionChain;
import com.ztf.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import com.ztf.types.enums.ResponseCode;
import com.ztf.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{

    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    /**
     * 思路分析：
     * 传入的参数为充值实体类，通过这个实体类可以获取到对应的sku对象
     * 通过sku就可以获取到对应的活动entity、活动count的entity
     * 将skuEntity对象、活动Entity、活动count的entity传入到action中进行过滤
     * 然后将上面的三个entity对象连同充值对象放入到buildOrderAggregate进行构建聚合对昂
     * 然后将聚合对象进行保存
     */
    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        //1.参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        //2.查询基础信息
        //2.1 通过购物车对象获取到sku实体类
        ActivitySkuEntity activitySkuEntity = queryActivitySku(skuRechargeEntity.getSku());
        //2.2 查询活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //2.3 查询次数信息
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        //3.活动规则校验
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        //4.构建订单聚合对象
        //这里为什么要传入充值对象，因为在sku中是没有userId的
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        //5.保存订单
        doSaveOrder(createOrderAggregate);

        //6.返回单号
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    //构建订单聚合对象
    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    //保存入库
    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);

}

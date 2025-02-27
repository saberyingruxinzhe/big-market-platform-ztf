package com.ztf.domain.activity.service;

import com.ztf.domain.activity.model.entity.SkuRechargeEntity;

//抽奖活动账户额度服务
public interface IRaffleActivityAccountQuotaService {
    //以sku创建抽奖活动订单，获得参与抽奖资格
    //入参为购物车实体类
    /**
     * 创建 sku 账户充值订单，给用户增加抽奖次数
     * <p>
     * 1. 在【打卡、签到、分享、对话、积分兑换】等行为动作下，创建出活动订单，给用户的活动账户【日、月】充值可用的抽奖次数。
     * 2. 对于用户可获得的抽奖次数，比如首次进来就有一次，则是依赖于运营配置的动作，在前端页面上。用户点击后，可以获得一次抽奖次数。
     */
    String createOrder(SkuRechargeEntity skuRechargeEntity);

    /**
     * 查询活动账户 - 日，参与次数
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 参与次数
     */
    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);
}

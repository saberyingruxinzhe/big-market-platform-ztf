package com.ztf.domain.activity.service;

import com.ztf.domain.activity.model.valobj.ActivitySkuStockKeyVO;

//抽奖活动SKU库存服务
public interface IRaffleActivitySkuStockService {
    /**
     * 获取活动sku库存消耗队列
     *
     * @return ActivitySkuStockKeyVO这个类就是在延迟队列中发送的那个类，所以这里从队列中取出的就是发送的那个类
     * 之前发送这个类是因为redis中库存扣减成功，所以发送一个延迟队列来延迟更新数据库
     * @throws InterruptedException 异常
     */
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 延迟队列 + 任务趋势更新活动sku库存
     *
     * @param sku 活动商品
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 缓存库存以消耗完毕，清空数据库库存
     *
     * @param sku 活动商品
     */
    void clearActivitySkuStock(Long sku);
}

package com.ztf.trigger.job;

import com.ztf.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.ztf.domain.activity.service.ISkuStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//更新活动sku库存任务
//这个方法为从延迟队列中取出相应的ActivitySkuStockKeyVO对象
//然后向sku数据库中进行相应的减一操作

//这个执行的是进行redis中的库存减一返回true的前提下，chain会发送一个延迟队列
//这个job就是用来监听那个延迟队列的，注意要和本节中新添加的listener相区别
@Slf4j
@Component()
public class UpdateActivitySkuStockJob {

    @Resource
    private ISkuStock skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("定时任务，更新活动sku库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (null == activitySkuStockKeyVO) return;
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，更新活动sku库存失败", e);
        }
    }

}

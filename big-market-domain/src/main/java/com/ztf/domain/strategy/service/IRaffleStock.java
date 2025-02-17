package com.ztf.domain.strategy.service;

import com.ztf.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

public interface IRaffleStock {
    //从奖品库存消费队列中获取到StrategyAwardStockKeyVO对象
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    //更新奖品库存消费记录(更新真正的数据库的数据！！！)
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}

package com.ztf.test;

import com.alibaba.fastjson2.JSON;
import com.ztf.infrastructure.persistent.dao.IRaffleActivityDao;
import com.ztf.infrastructure.persistent.dao.IStrategyAwardDao;
import com.ztf.infrastructure.persistent.po.RaffleActivity;
import com.ztf.infrastructure.persistent.po.StrategyAward;
import com.ztf.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IRedisService redisService;
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Test
    public void test() {
        StrategyAward strategyAwardReq = new StrategyAward();

        strategyAwardReq.setAwardId(104);
        strategyAwardReq.setStrategyId(100006L);
        StrategyAward strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAwardReq);
        log.info("测试结果：{}", JSON.toJSONString(strategyAwardRes));

    }

}

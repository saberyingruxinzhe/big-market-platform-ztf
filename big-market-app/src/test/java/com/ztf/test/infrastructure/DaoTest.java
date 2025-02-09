package com.ztf.test.infrastructure;


import com.alibaba.fastjson.JSON;
import com.ztf.infrastructure.persistent.dao.IAwardDao;
import com.ztf.infrastructure.persistent.dao.IStrategyAwardDao;
import com.ztf.infrastructure.persistent.dao.IStrategyDao;
import com.ztf.infrastructure.persistent.dao.IStrategyRuleDao;
import com.ztf.infrastructure.persistent.po.Award;
import com.ztf.infrastructure.persistent.po.Strategy;
import com.ztf.infrastructure.persistent.po.StrategyAward;
import com.ztf.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoTest {
    @Resource
    private IAwardDao awardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;


    @Test
    public void test_queryAwardList(){
        List<Award> awards = awardDao.queryAwardList();
        List<Strategy> strategies = strategyDao.queryStrategyList();
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();


        log.info("测试结果: {}", JSON.toJSONString(awards));
        log.info("测试结果: {}", JSON.toJSONString(strategies));
        log.info("测试结果: {}", JSON.toJSONString(strategyAwards));
        log.info("测试结果: {}", JSON.toJSONString(strategyRules));
    }

}

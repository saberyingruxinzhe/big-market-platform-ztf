package com.ztf.test.infrastructure;


import com.alibaba.fastjson.JSON;
import com.ztf.infrastructure.persistent.dao.IAwardDao;
import com.ztf.infrastructure.persistent.dao.IStrategyDao;
import com.ztf.infrastructure.persistent.dao.IStrategyRuleDao;
import com.ztf.infrastructure.persistent.po.Award;
import com.ztf.infrastructure.persistent.po.Strategy;
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
public class AwardDaoTest {
    @Resource
    private IAwardDao awardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void test_queryAwardList(){
        List<Award> awards = awardDao.queryAwardList();
        log.info("测试结果: {}", JSON.toJSONString(awards));
    }

    @Test
    public void test_queryStrategyByStrategyId(){
        Strategy strategy = strategyDao.queryStrategyByStrategyId(100001L);
        log.info("测试结果：{}", JSON.toJSONString(strategy));
    }

    @Test
    public void test_queryStrategyRuleBy(){
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(100001L);
        strategyRuleReq.setRuleModel("rule_weight");
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        log.info("测试结果：{}", JSON.toJSONString(strategyRuleRes));
    }

}

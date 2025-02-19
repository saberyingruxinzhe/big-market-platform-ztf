package com.ztf.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    public static class RedisKey {
        public static String STRATEGY_KEY = "big_market_strategy_key_";

        //有两个作用：从strategy_award表格中通过和strategyId结合获得奖品列表，以及通过和strategyId和awardId结合获取单个奖品
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String RULE_TREE_VO_KEY = "rule_tree_vo_key_";
        //这个key的用法：
        // Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        //这样可以组成redis中存放奖品库存的key
        public static String STRATEGY_AWARD_COUNT_KEY = "strategy_award_count_key_";
        //这个key使用向redis中奖品库存消费队列中存取StrategyAwardStockKeyVO对象的
        public static String STRATEGY_AWARD_COUNT_QUERY_KEY = "strategy_award_count_query_key";
    }

}

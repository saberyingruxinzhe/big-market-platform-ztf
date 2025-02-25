package com.ztf.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    public static class RedisKey {
        /**
         * 活动领域
         */
        public static String ACTIVITY_KEY = "big_market_activity_key_";
        public static String ACTIVITY_SKU_KEY = "big_market_activity_sku_key_";
        public static String ACTIVITY_COUNT_KEY = "big_market_activity_count_key_";


        //这个是用来发送延迟队列用来获取到延迟队列的【延迟队列也是从redis中获取的】
        public static String ACTIVITY_SKU_COUNT_QUERY_KEY = "activity_sku_count_query_key";
        //预热sku库存使用的key
        public static String ACTIVITY_SKU_STOCK_COUNT_KEY = "activity_sku_stock_count_key_";

        /**
         * 策略领域
         */
        public static String STRATEGY_KEY = "big_market_strategy_key_";

        //有两个作用：从strategy_award表格中通过和strategyId结合获得奖品列表，以及通过和strategyId和awardId结合获取单个奖品
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        //用来从缓存中获取到strategy_award信息，用于前端转盘或者九宫格奖品信息显示的
        public static String STRATEGY_AWARD_LIST_KEY = "big_market_strategy_award_list_key_";
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

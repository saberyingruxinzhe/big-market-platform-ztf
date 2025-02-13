package com.ztf.domain.strategy.model.entity;


import com.ztf.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.*;

//规则动作实体类，用来返回抽奖前、中、后的过滤后的实体对象，通过这个实体对象，可以获取到最终的奖品实体类
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {
    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();;
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    //区分抽奖前、中、后的内部类
    static public class RaffleEntity {

    }

    //抽奖前的RaffleEntity实体类
    //这个实体类并不是专用于权重抽奖的，黑名单也是用这个实体类
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        //策略id
        private Long strategyId;

        //权重值Key；用于抽奖时可以选择权重抽奖。
        //解释一下这个值的具体含义，就是filter会去将用户的积分和各个积分做对比来决定哪一个权重是最合适的
        //对比是靠一个map来实现的，map的key为4000，value为4000:102,103,104,105
        //这里的ruleWeightValueKey就是确定好哪一个权重之后获取到的map的value，即4000:102,103,104,105
        //这个值有什么作用呢？之后抽奖的时候需要使用这个值来获取到对应redis中的信息。
        //即strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey)
        private String ruleWeightValueKey;

        //奖品id
        private Integer awardId;
    }

    //抽奖前的RaffleEntity实体类
    static public class RaffleCenterEntity extends RaffleEntity{

    }

    //抽奖后的RaffleEntity实体类
    static public class RaffleAfterEntity extends RaffleEntity{

    }
}

package com.ztf.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//抽奖后最终返回的奖品实体类
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
    //奖品id
    private Integer awardId;

    //奖品配置信息
    private String awardConfig;

    //奖品顺序号
    private Integer sort;
}

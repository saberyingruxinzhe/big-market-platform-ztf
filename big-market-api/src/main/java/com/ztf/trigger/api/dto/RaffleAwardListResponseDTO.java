package com.ztf.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {
    // 奖品ID
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则 - 抽奖N次后解锁，未配置则为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 - true 已解锁、false 未解锁【方便前端对于解锁以及未解锁的奖品图标进行更改】
    private Boolean isAwardUnlock;
    // 等待解锁次数 - 规定的抽奖N次解锁减去用户已经抽奖次数【方便前端设置还要抽几次就可以解锁】
    private Integer waitUnLockCount;
}

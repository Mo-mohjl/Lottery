package com.swpu.lottery.domain.strategy.service.algorithm;

import com.swpu.lottery.domain.strategy.model.vo.AwardRateInfo;

import java.util.List;

public interface IDrawAlgorithm {
    /**
     * 策略ID
     * awardRateInfoList 奖品概率配置集合 「值示例：AwardRateInfo.awardRate = 0.04」
     */
    void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList);
    /**
     * 判断是否已经，做了数据初始化
     */
    boolean isExistRateTuple(Long strategyId);

    String randomDraw(Long strategyId,List<String> excludeAwardIds);

    void entireInitRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList);
}

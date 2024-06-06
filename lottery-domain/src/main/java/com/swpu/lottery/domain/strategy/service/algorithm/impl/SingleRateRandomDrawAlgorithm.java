package com.swpu.lottery.domain.strategy.service.algorithm.impl;

import com.swpu.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleRateRandomDrawAlgorithm extends BaseAlgorithm {
    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        String[] rateTuple=super.rateTupleMap.get(strategyId);
        assert rateTuple!=null;
        int randomVal = this.generateSecureRandomIntCode(100);
        int idx=super.hashIdx(randomVal);

        String awardId = rateTuple[idx];
        if(excludeAwardIds.contains(awardId)){
            return null;
        }
        return awardId;
    }
}

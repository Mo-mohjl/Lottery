package com.swpu.lottery.domain.strategy.service.algorithm.impl;

import com.swpu.lottery.domain.strategy.model.vo.AwardRateInfo;
import com.swpu.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntiretyRateRandomDrawAlgorithm extends BaseAlgorithm{
    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        BigDecimal differenceDenominator=BigDecimal.ZERO;
        List<AwardRateInfo> differenceAwardRateList=new ArrayList<>();
        List<AwardRateInfo> awardRateIntervalValList=awardRateInfoMap.get(strategyId);
        for (AwardRateInfo awardRateInfo:awardRateIntervalValList) {
            String awardId = awardRateInfo.getAwardId();
            if (excludeAwardIds.contains(awardId)) {
                continue;
            }
            differenceAwardRateList.add(awardRateInfo);
            differenceDenominator=differenceDenominator.add(awardRateInfo.getAwardRate());
        }
        if(differenceAwardRateList.size()==0){
            return null;
        }
        if(differenceAwardRateList.size()==1){
            return differenceAwardRateList.get(0).getAwardId();
        }
        int randomVal=this.generateSecureRandomIntCode(100);
        String awardId=null;
        int cursorVal = 0;
        for(AwardRateInfo awardRateInfo:differenceAwardRateList){
            int rateVal=awardRateInfo.getAwardRate().divide(differenceDenominator,2,BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).intValue();
            if(randomVal<=(cursorVal+rateVal)){
                awardId=awardRateInfo.getAwardId();
                break;
            }
            cursorVal+=rateVal;
        }
        return awardId;
    }
}

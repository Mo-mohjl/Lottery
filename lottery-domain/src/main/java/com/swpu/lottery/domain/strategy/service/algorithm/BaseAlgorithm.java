package com.swpu.lottery.domain.strategy.service.algorithm;

import com.swpu.lottery.domain.strategy.model.vo.AwardRateInfo;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseAlgorithm implements IDrawAlgorithm{
    private final int HASH_INCREMENT=0x61c88647;
    private final int RATE_TUPLE_LENGTH = 128;
    protected Map<Long,String[]> rateTupleMap = new ConcurrentHashMap<>();
    protected Map<Long,List<AwardRateInfo>> awardRateInfoMap=new ConcurrentHashMap<>();
    @Override
    public void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList) {
        awardRateInfoMap.put(strategyId,awardRateInfoList);
        String[] rateTuple=rateTupleMap.computeIfAbsent(strategyId, k -> new String[RATE_TUPLE_LENGTH]);
        int cursorVal = 0;
        for(AwardRateInfo awardRateInfo:awardRateInfoList){
            int rateVal=awardRateInfo.getAwardRate().multiply(new BigDecimal(100)).intValue();
            for(int i=cursorVal+1;i<=(rateVal+cursorVal);i++){
                rateTuple[hashIdx(i)]=awardRateInfo.getAwardId();
            }
            cursorVal += rateVal;
        }
    }
    public void entireInitRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList){
        awardRateInfoMap.put(strategyId,awardRateInfoList);
    }
    protected int hashIdx(int val) {
        int hashCode=val * HASH_INCREMENT + HASH_INCREMENT;
        return hashCode & (RATE_TUPLE_LENGTH-1);
    }

    @Override
    public boolean isExistRateTuple(Long strategyId) {
        return rateTupleMap.containsKey(strategyId);
    }

    protected int generateSecureRandomIntCode(int bound){
        return new SecureRandom().nextInt(bound) + 1;
    }
}

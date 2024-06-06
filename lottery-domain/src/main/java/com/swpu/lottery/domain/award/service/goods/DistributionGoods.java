package com.swpu.lottery.domain.award.service.goods;

import com.swpu.lottery.domain.award.repository.IAwardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class DistributionGoods {
    public Logger logger= LoggerFactory.getLogger(DistributionGoods.class);
    @Resource
    private IAwardRepository awardRepository;
    protected void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState){
        awardRepository.updateUserAwardState(uId, orderId, awardId, grantState);
        logger.info("TODO 后期添加更新分库分表中，用户个人的抽奖记录表中奖品发奖状态 uId：{}", uId);
    }
}

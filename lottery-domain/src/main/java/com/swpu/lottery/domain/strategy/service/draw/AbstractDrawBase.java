package com.swpu.lottery.domain.strategy.service.draw;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.strategy.model.aggregates.StrategyRich;
import com.swpu.lottery.domain.strategy.model.req.DrawReq;
import com.swpu.lottery.domain.strategy.model.res.DrawResult;
import com.swpu.lottery.domain.strategy.model.vo.*;
import com.swpu.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import com.swpu.lottery.domain.strategy.vo.DrawAwardVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDrawBase extends DrawStrategySupport implements IDrawExec{
    private Logger logger=LoggerFactory.getLogger(AbstractDrawBase.class);
    @Override
    public DrawResult doDrawExec(DrawReq req) {
        StrategyRich strategyRich=super.queryStrategyRich(req.getStrategyId());
        StrategyBriefVO strategy=strategyRich.getStrategy();
        this.checkAndInitRateData(req.getStrategyId(), strategy.getStrategyMode(), strategyRich.getStrategyDetailList());
        List<String> excludeAwardIds = this.queryExcludeAwardIds(req.getStrategyId());
        String awardId = this.drawAlgorithm(req.getStrategyId(), drawAlgorithmGroup.get(strategy.getStrategyMode()), excludeAwardIds);
        return buildDrawResult(req.getuId(), req.getStrategyId(), awardId);
    }
    protected abstract List<String> queryExcludeAwardIds(Long strategyId);

    protected abstract String drawAlgorithm(Long strategyId, IDrawAlgorithm drawAlgorithm, List<String> excludeAwardIds);
    private void checkAndInitRateData(Long strategyId, Integer strategyMode, List<StrategyDetailBriefVO> strategyDetailList){
        IDrawAlgorithm iDrawAlgorithm=drawAlgorithmGroup.get(strategyMode);
        if (iDrawAlgorithm.isExistRateTuple(strategyId)){
            return;
        }
        List<AwardRateInfo> awardRateInfoList=new ArrayList<>(strategyDetailList.size());
        for(StrategyDetailBriefVO strategyDetail:strategyDetailList){
            awardRateInfoList.add(new AwardRateInfo(strategyDetail.getAwardId(),strategyDetail.getAwardRate()));
        }
        if(!Constants.StrategyMode.SINGLE.getCode().equals(strategyMode)){
            iDrawAlgorithm.entireInitRateTuple(strategyId,awardRateInfoList);
            return;
        }
        iDrawAlgorithm.initRateTuple(strategyId, awardRateInfoList);
    }
    private DrawResult buildDrawResult(String uId, Long strategyId, String awardId) {
        if (null == awardId) {
            logger.info("执行策略抽奖完成【未中奖】，用户：{} 策略ID：{}", uId, strategyId);
            return new DrawResult(uId, strategyId, Constants.DrawState.FAIL.getCode());
        }
        AwardBriefVO award = super.queryAwardInfo(awardId);
        DrawAwardVO drawAwardVO = new DrawAwardVO(uId,award.getAwardId(),award.getAwardType(),award.getAwardName(),award.getAwardContent());
        logger.info("执行策略抽奖完成【已中奖】，用户：{} 策略ID：{} 奖品ID：{} 奖品名称：{}", uId, strategyId, awardId, award.getAwardName());
        return new DrawResult(uId, strategyId, Constants.DrawState.SUCCESS.getCode(), drawAwardVO);
    }
}

package com.swpu.lottery.domain.activity.service.partake;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.common.Result;
import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.res.PartakeResult;
import com.swpu.lottery.domain.activity.model.res.StockResult;
import com.swpu.lottery.domain.activity.model.vo.ActivityBillVO;
import com.swpu.lottery.domain.activity.model.vo.UserTakeActivityVO;
import com.swpu.lottery.domain.support.ids.IIdGenerator;

import javax.annotation.Resource;
import java.util.Map;

public abstract class BaseActivityPartake extends ActivityPartakeSupport implements IActivityPartake {
    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;
    @Override
    public PartakeResult doPartake(PartakeReq req) {
        //查询是否存在未使用的抽奖单
        UserTakeActivityVO userTakeActivityVO = this.queryNoConsumedTakeActivityOrder(req.getuId(), req.getActivityId());
        if(null!=userTakeActivityVO.getStrategyId()){
            return buildPartakeResult(userTakeActivityVO.getTakeId(),userTakeActivityVO.getStrategyId(),Constants.ResponseCode.NOT_CONSUMED_TAKE);
        }
        //查询活动账单信息
        ActivityBillVO activityBillVO = super.queryActivityBill(req);
        // 活动信息校验处理【活动库存、状态、日期、个人参与次数】
        Result checkResult = this.checkActivityBill(req, activityBillVO);
        if(!checkResult.getCode().equals(Constants.ResponseCode.SUCCESS.getCode())){
            return new PartakeResult(checkResult.getCode(), checkResult.getInfo());
        }
        // 扣减活动库存，通过Redis【活动库存扣减编号，作为锁的Key，缩小颗粒度】 Begin
        StockResult subtractionActivityResult = this.subtractionActivityStockByRedis(req.getuId(), req.getActivityId(), activityBillVO.getStockCount());
        if(!Constants.ResponseCode.SUCCESS.getCode().equals(subtractionActivityResult.getCode())){
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), subtractionActivityResult.getCode());
            return new PartakeResult(subtractionActivityResult.getCode(), subtractionActivityResult.getInfo());
        }
        // 领取活动信息【个人用户把活动信息写入到用户表】
        Long takeId=idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        Long strategyId=activityBillVO.getStrategyId();
        Result grabResult = this.grabActivity(req, activityBillVO, takeId, strategyId);
        if(!Constants.ResponseCode.SUCCESS.getCode().equals(grabResult.getCode())){
            this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), subtractionActivityResult.getCode());
            return new PartakeResult(grabResult.getCode(), grabResult.getInfo());
        }
        // 扣减活动库存，通过Redis End
        this.recoverActivityCacheStockByRedis(req.getActivityId(), subtractionActivityResult.getStockKey(), Constants.ResponseCode.SUCCESS.getCode());
        return buildPartakeResult(activityBillVO.getStrategyId(),takeId,activityBillVO.getStockCount(),activityBillVO.getStockSurplusCount(),Constants.ResponseCode.SUCCESS);
    }
    private PartakeResult buildPartakeResult(Long strategyId, Long takeId, Integer stockCount, Integer stockSurplusCount, Constants.ResponseCode code) {
        PartakeResult partakeResult = new PartakeResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        partakeResult.setTakeId(takeId);
        partakeResult.setStrategyId(strategyId);
        partakeResult.setStockCount(stockCount);
        partakeResult.setStockSurplusCount(stockSurplusCount);
        return partakeResult;
    }
    public PartakeResult buildPartakeResult(Long takeId,Long strategyId,Constants.ResponseCode code){
        PartakeResult partakeResult = new PartakeResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        partakeResult.setTakeId(takeId);
        partakeResult.setStrategyId(strategyId);
        return partakeResult;
    }
    /**
     * 活动信息校验处理，把活动库存、状态、日期、个人参与次数
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 校验结果
     */
    protected abstract Result checkActivityBill(PartakeReq partake, ActivityBillVO bill);
    /**
     * 扣减活动库存
     *
     * @param req 参与活动请求
     * @return 扣减结果
     */
    protected abstract Result subtractionActivityStock(PartakeReq req);
    /**
     * 扣减活动库存，通过Redis
     *
     * @param uId        用户ID
     * @param activityId 活动号
     * @param stockCount 总库存
     * @return 扣减结果
     */
    protected abstract StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount);

    /**
     * 恢复活动库存，通过Redis 【如果非常异常，则需要进行缓存库存恢复，只保证不超卖的特性，所以不保证一定能恢复占用库存，另外最终可以由任务进行补偿库存】
     *
     * @param activityId 活动ID
     * @param tokenKey   分布式 KEY 用于清理
     * @param code       状态
     */
    protected abstract void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code);

    /**
     * 领取活动
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 领取结果
     */
    protected abstract Result grabActivity(PartakeReq partake, ActivityBillVO bill, Long takeId, Long strategyId);
}

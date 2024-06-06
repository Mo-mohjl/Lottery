package com.swpu.lottery.infra.repository;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.res.StockResult;
import com.swpu.lottery.domain.activity.model.vo.*;
import com.swpu.lottery.domain.activity.repository.IActivityRepository;
import com.swpu.lottery.domain.activity.repository.IUserTakeActivityRepository;
import com.swpu.lottery.domain.support.ids.IIdGenerator;
import com.swpu.lottery.infra.dao.*;
import com.swpu.lottery.infra.po.*;
import com.swpu.lottery.infra.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ActivityRepository implements IActivityRepository {
    private Logger logger= LoggerFactory.getLogger(ActivityRepository.class);
    @Resource
    private IActivityDao activityDao;
    @Resource
    private IAwardDao awardDao;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyDetailDao strategyDetailDao;
    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void addActivity(ActivityVO activity) {
        Activity act=new Activity();
        BeanUtils.copyProperties(activity,act);
        activityDao.insert(act);
    }

    @Override
    public void addAward(List<AwardVO> awardList) {
        List<Award> awa=new ArrayList<>();
        for(AwardVO awardVO:awardList){
            Award award=new Award();
            BeanUtils.copyProperties(awardVO,award);
            awa.add(award);
        }
        awardDao.insertList(awa);
    }

    @Override
    public void addStrategy(StrategyVO strategy) {
        Strategy strate = new Strategy();
        BeanUtils.copyProperties(strategy,strate);
        strategyDao.insert(strate);
    }

    @Override
    public void addStrategyDetailList(List<StrategyDetailVO> strategyDetailList) {
        List<StrategyDetail> strategyDetails=new ArrayList<>();
        for(StrategyDetailVO strategyDetailVO:strategyDetailList){
            StrategyDetail strategyDetail = new StrategyDetail();
            BeanUtils.copyProperties(strategyDetailVO,strategyDetail);
            strategyDetails.add(strategyDetail);
        }
        strategyDetailDao.insertList(strategyDetails);
    }

    @Override
    public boolean alterStatus(Long activityId, Enum<Constants.ActivityState> beforeState, Enum<Constants.ActivityState> afterState) {
        AlterStateVO alterStateVO = new AlterStateVO();
        alterStateVO.setActivityId(activityId);
        alterStateVO.setBeforeState(((Constants.ActivityState) beforeState).getCode());
        alterStateVO.setAfterState(((Constants.ActivityState) afterState).getCode());
        int i = activityDao.alterState(alterStateVO);
        return 1==i;
    }

    @Override
    public ActivityBillVO queryActivityBill(PartakeReq req) {
        Activity activity = activityDao.queryActivityById(req.getActivityId());
        UserTakeActivityCount userTakeActivityCountReq = new UserTakeActivityCount();
        userTakeActivityCountReq.setuId(req.getuId());
        userTakeActivityCountReq.setActivityId(req.getActivityId());
        UserTakeActivityCount userTakeActivityCount = userTakeActivityCountDao.queryUserTakeActivityCount(userTakeActivityCountReq);
        ActivityBillVO activityBillVO = new ActivityBillVO();
        activityBillVO.setuId(req.getuId());
        activityBillVO.setActivityId(req.getActivityId());
        activityBillVO.setActivityName(activity.getActivityName());
        activityBillVO.setBeginDateTime(activity.getBeginDateTime());
        activityBillVO.setEndDateTime(activity.getEndDateTime());
        activityBillVO.setTakeCount(activity.getTakeCount());
        activityBillVO.setStockCount(activity.getStockCount());
        activityBillVO.setStockSurplusCount(activity.getStockSurplusCount());
        activityBillVO.setStrategyId(activity.getStrategyId());
        activityBillVO.setState(activity.getState());
        activityBillVO.setUserTakeLeftCount(null == userTakeActivityCount ? null : userTakeActivityCount.getLeftCount());
        return activityBillVO;
    }

    @Override
    public int subtractionActivityStock(Long activityId) {
        int i = activityDao.subtractionActivityStock(activityId);
        return i;
    }

    @Override
    public List<ActivityVO> scanToDoActivityList(long id) {
        List<ActivityVO> activityVOs=new ArrayList<>();
        List<Activity> activities = activityDao.scanToDoActivityList(id);
        for (Activity activity:activities) {
            ActivityVO activityVO=new ActivityVO();
            BeanUtils.copyProperties(activity,activityVO);
            activityVOs.add(activityVO);
        }
        return activityVOs;
    }

    @Override
    public StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount) {
        String stockKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT(activityId);
        Integer stockUsedCount  =(int) redisUtil.incr(stockKey, 1);
        if(stockUsedCount>stockCount){
            redisUtil.decr(stockKey,1);
            return new StockResult(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
        String stockTokenKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT_TOKEN(activityId, stockUsedCount);
        boolean lockToken = redisUtil.setNx(stockTokenKey, 350L);
        if (!lockToken) {
            logger.info("抽奖活动{}用户秒杀{}扣减库存，分布式锁失败：{}", activityId, uId, stockTokenKey);
            return new StockResult(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
        return new StockResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), stockTokenKey, stockCount - stockUsedCount);
    }

    @Override
    public void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code) {
        redisUtil.del(tokenKey);
    }
}

package com.swpu.lottery.infra.repository;

import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.model.vo.DrawOrderVO;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import com.swpu.lottery.domain.activity.model.vo.UserTakeActivityVO;
import com.swpu.lottery.domain.activity.repository.IUserTakeActivityRepository;
import com.swpu.lottery.infra.dao.IActivityDao;
import com.swpu.lottery.infra.dao.IUserStrategyExportDao;
import com.swpu.lottery.infra.dao.IUserTakeActivityCountDao;
import com.swpu.lottery.infra.dao.IUserTakeActivityDao;
import com.swpu.lottery.infra.po.Activity;
import com.swpu.lottery.infra.po.UserStrategyExport;
import com.swpu.lottery.infra.po.UserTakeActivity;
import com.swpu.lottery.infra.po.UserTakeActivityCount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserTakeActivityRepository implements IUserTakeActivityRepository {
    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;
    @Resource
    private IUserTakeActivityDao userTakeActivityDao;
    @Resource
    private IUserStrategyExportDao userStrategyExportDao;
    @Resource
    private IActivityDao activityDao;
    @Override
    public int subtractionLeftCount(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date partakeDate) {
        if(null==userTakeLeftCount){
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setActivityId(activityId);
            userTakeActivityCount.setTotalCount(takeCount);
            userTakeActivityCount.setuId(uId);
            userTakeActivityCount.setLeftCount(takeCount-1);
            userTakeActivityCountDao.insert(userTakeActivityCount);
            return 1;
        }else {
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setuId(uId);
            userTakeActivityCount.setActivityId(activityId);
            return userTakeActivityCountDao.updateLeftCount(userTakeActivityCount);
        }
    }

    @Override
    public void takeActivity(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date takeDate, Long takeId,Long strategyId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setActivityName(activityName);
        userTakeActivity.setTakeId(takeId);
        userTakeActivity.setTakeDate(takeDate);
        userTakeActivity.setuId(uId);
        if(null==userTakeLeftCount){
            userTakeActivity.setTakeCount(1);
        }else {
            userTakeActivity.setTakeCount(takeCount-userTakeLeftCount);
        }
        String uuid=uId + "_" + activityId + "_" + userTakeActivity.getTakeCount();
        userTakeActivity.setUuid(uuid);
        userTakeActivity.setStrategyId(strategyId);
        userTakeActivity.setState(1);
        userTakeActivityDao.insert(userTakeActivity);
    }
    @Override
    public UserTakeActivityVO queryNoConsumedTakeActivityOrder(String uId,Long activityId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setuId(uId);
        userTakeActivity.setActivityId(activityId);
        UserTakeActivity userTakeActivityReq = userTakeActivityDao.queryNoConsumedTakeActivityOrder(userTakeActivity);
        UserTakeActivityVO userTakeActivityVO = new UserTakeActivityVO();
        if(userTakeActivityReq==null){
            return userTakeActivityVO;
        }
        BeanUtils.copyProperties(userTakeActivityReq,userTakeActivityVO);
        return userTakeActivityVO;
    }

    @Override
    public int lockTackActivity(String uId, Long activityId, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setuId(uId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setTakeId(takeId);
        int count=userTakeActivityDao.lockTackActivity(userTakeActivity);
        return count;
    }

    @Override
    public void saveUserStrategyExport(DrawOrderVO drawOrder) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        BeanUtils.copyProperties(drawOrder,userStrategyExport);
        userStrategyExportDao.insert(userStrategyExport);
    }

    @Override
    public void updateInvoiceMqState(String uId, Long orderId, Integer mqState) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId(uId);
        userStrategyExport.setOrderId(orderId);
        userStrategyExport.setMqState(mqState);
        userStrategyExportDao.updateInvoiceMqState(userStrategyExport);
    }

    @Override
    public List<InvoiceVO> scanInvoiceMqState() {
        // 查询发送MQ失败和超时30分钟，未发送MQ的数据
        List<UserStrategyExport> userStrategyExportList = userStrategyExportDao.scanInvoiceMqState();
        // 转换对象
        List<InvoiceVO> invoiceVOList = new ArrayList<>(userStrategyExportList.size());
        for (UserStrategyExport userStrategyExport : userStrategyExportList) {
            InvoiceVO invoiceVO = new InvoiceVO();
            invoiceVO.setuId(userStrategyExport.getuId());
            invoiceVO.setOrderId(userStrategyExport.getOrderId());
            invoiceVO.setAwardId(userStrategyExport.getAwardId());
            invoiceVO.setAwardType(userStrategyExport.getAwardType());
            invoiceVO.setAwardName(userStrategyExport.getAwardName());
            invoiceVO.setAwardContent(userStrategyExport.getAwardContent());
            invoiceVOList.add(invoiceVO);
        }
        return invoiceVOList;
    }

    @Override
    public void updateActivityStock(ActivityPartakeRecordVO activityPartakeRecordVO) {
        Activity activity=new Activity();
        activity.setActivityId(activityPartakeRecordVO.getActivityId());
        activity.setStockSurplusCount(activityPartakeRecordVO.getStockSurplusCount());
        activityDao.updateActivityStock(activity);
    }
}

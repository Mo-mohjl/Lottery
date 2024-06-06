package com.swpu.lottery.domain.activity.repository;

import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.model.vo.DrawOrderVO;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import com.swpu.lottery.domain.activity.model.vo.UserTakeActivityVO;

import java.util.Date;
import java.util.List;

public interface IUserTakeActivityRepository {
    /**
     * 扣减个人活动参与次数
     *
     * @param activityId        活动ID
     * @param activityName      活动名称
     * @param takeCount         活动个人可领取次数
     * @param userTakeLeftCount 活动个人剩余领取次数
     * @param uId               用户ID
     * @param partakeDate       领取时间
     * @return                  更新结果
     */
    int subtractionLeftCount(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date partakeDate);
    /**
     * 领取活动
     *
     * @param activityId        活动ID
     * @param activityName      活动名称
     * @param takeCount         活动个人可领取次数
     * @param userTakeLeftCount 活动个人剩余领取次数
     * @param uId               用户ID
     * @param takeDate          领取时间
     * @param takeId            领取ID
     */
    void takeActivity(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId, Date takeDate, Long takeId, Long strategyId);

    UserTakeActivityVO queryNoConsumedTakeActivityOrder(String uId,Long activityId);

    int lockTackActivity(String uId, Long activityId, Long takeId);

    void saveUserStrategyExport(DrawOrderVO drawOrder);

    /**
     * 更新发货单MQ状态
     *
     * @param uId     用户ID
     * @param orderId 订单ID
     * @param mqState MQ 发送状态
     */
    void updateInvoiceMqState(String uId, Long orderId, Integer mqState);

    List<InvoiceVO> scanInvoiceMqState();

    void updateActivityStock(ActivityPartakeRecordVO activityPartakeRecordVO);
}

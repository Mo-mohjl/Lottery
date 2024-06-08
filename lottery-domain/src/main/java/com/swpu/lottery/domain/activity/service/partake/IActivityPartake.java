package com.swpu.lottery.domain.activity.service.partake;


import com.swpu.lottery.common.Result;
import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.res.PartakeResult;
import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.model.vo.DrawOrderVO;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import com.swpu.lottery.domain.activity.model.vo.UserTakeActivityVO;

import java.util.List;

public interface IActivityPartake {

    /**
     * 参与活动
     * @param req 入参
     * @return    领取结果
     */
    PartakeResult doPartake(PartakeReq req);
    /**
     * 保存奖品单
     * @param drawOrder 奖品单
     * @return          保存结果
     */
    Result recordDrawOrder(DrawOrderVO drawOrder);

    /**
     * 查询是否有未使用的抽奖单
     * @param uId
     * @param activityId
     * @return
     */
    UserTakeActivityVO queryNoConsumedTakeActivityOrder(String uId, Long activityId);
    /**
     * 更新发货单MQ状态
     *  @param uId      用户ID
     * @param orderId   订单ID
     * @param mqState   MQ 发送状态
     */
    void updateInvoiceMqState(String uId, Long orderId, Integer mqState);

    List<InvoiceVO>  scanInvoiceMqState(int dbCount,int tbCount);

    void updateActivityStock(ActivityPartakeRecordVO activityPartakeRecordVO);
}

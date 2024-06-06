package com.swpu.lottery.domain.activity.service.partake;

import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.vo.ActivityBillVO;
import com.swpu.lottery.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;

public class ActivityPartakeSupport {
    @Resource
    protected IActivityRepository activityRepository;
    protected ActivityBillVO queryActivityBill(PartakeReq req){
        return activityRepository.queryActivityBill(req);
    }
}

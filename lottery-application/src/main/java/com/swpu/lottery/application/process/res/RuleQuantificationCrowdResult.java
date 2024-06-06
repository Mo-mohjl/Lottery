package com.swpu.lottery.application.process.res;

import com.swpu.lottery.common.Result;

public class RuleQuantificationCrowdResult extends Result {

    private Long activityId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public RuleQuantificationCrowdResult(String code, String info) {
        super(code, info);
    }
}

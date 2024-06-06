package com.swpu.lottery.domain.activity.model.aggregates;

import com.swpu.lottery.domain.activity.model.vo.ActivityVO;

import java.util.List;

public class ActivityInfoLimitPageRich {
    private Long count;
    private List<ActivityVO> activityVOList;
    public ActivityInfoLimitPageRich() {
    }
    public ActivityInfoLimitPageRich(Long count, List<ActivityVO> activityVOList) {
        this.count = count;
        this.activityVOList = activityVOList;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public List<ActivityVO> getActivityVOList() {
        return activityVOList;
    }
    public void setActivityVOList(List<ActivityVO> activityVOList) {
        this.activityVOList = activityVOList;
    }
}

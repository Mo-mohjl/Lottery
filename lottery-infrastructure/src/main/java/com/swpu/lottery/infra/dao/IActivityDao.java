package com.swpu.lottery.infra.dao;

import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.vo.ActivityBillVO;
import com.swpu.lottery.domain.activity.model.vo.AlterStateVO;
import com.swpu.lottery.infra.po.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IActivityDao {
    void insert(Activity act);
    Activity queryActivityById(Long activityId);
    int alterState(AlterStateVO alterStateVO);
    int subtractionActivityStock(Long activityId);
    List<Activity> scanToDoActivityList(Long id);
    void updateActivityStock(Activity activity);
}

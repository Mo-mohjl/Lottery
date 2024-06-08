package com.swpu.lottery.infra.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.swpu.lottery.domain.activity.model.vo.UserTakeActivityVO;
import com.swpu.lottery.infra.po.UserTakeActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserTakeActivityDao {
    /**
     * 插入用户领取活动信息
     *
     * @param userTakeActivity 入参
     */
    @DBRouter(key = "uId")
    void insert(UserTakeActivity userTakeActivity);
    @DBRouter(key = "uId")
    UserTakeActivity queryNoConsumedTakeActivityOrder(UserTakeActivity userTakeActivity);
    @DBRouter(key = "uId")
    int lockTackActivity(UserTakeActivity userTakeActivity);
}
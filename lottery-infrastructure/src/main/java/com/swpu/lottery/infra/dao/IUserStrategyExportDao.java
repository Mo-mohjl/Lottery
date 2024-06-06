package com.swpu.lottery.infra.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.swpu.lottery.infra.po.UserStrategyExport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserStrategyExportDao {
    /**
     * 新增数据
     * @param userStrategyExport 用户策略
     */
    @DBRouter(key = "uId")
    void insert(UserStrategyExport userStrategyExport);
    /**
     * 查询数据
     * @param uId 用户ID
     * @return 用户策略
     */
    @DBRouter
    UserStrategyExport queryUserStrategyExportByUId(String uId);

    @DBRouter
    void updateInvoiceMqState(UserStrategyExport userStrategyExport);
    @DBRouter
    void updateUserAwardState(UserStrategyExport userStrategyExport);

    List<UserStrategyExport> scanInvoiceMqState();
}

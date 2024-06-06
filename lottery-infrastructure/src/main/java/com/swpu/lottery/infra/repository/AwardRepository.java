package com.swpu.lottery.infra.repository;

import com.swpu.lottery.domain.award.repository.IAwardRepository;
import com.swpu.lottery.infra.dao.IUserStrategyExportDao;
import com.swpu.lottery.infra.po.UserStrategyExport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AwardRepository implements IAwardRepository {
    @Resource
    private IUserStrategyExportDao userStrategyExportDao;
    @Override
    public void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId(uId);
        userStrategyExport.setOrderId(orderId);
        userStrategyExport.setAwardId(awardId);
        userStrategyExport.setGrantState(grantState);
        userStrategyExportDao.updateUserAwardState(userStrategyExport);
    }
}

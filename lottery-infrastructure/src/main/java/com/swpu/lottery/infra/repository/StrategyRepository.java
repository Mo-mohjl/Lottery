package com.swpu.lottery.infra.repository;

import com.swpu.lottery.domain.strategy.model.aggregates.StrategyRich;
import com.swpu.lottery.domain.strategy.model.vo.AwardBriefVO;
import com.swpu.lottery.domain.strategy.model.vo.StrategyBriefVO;
import com.swpu.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
import com.swpu.lottery.domain.strategy.repository.IStrategyRepository;
import com.swpu.lottery.infra.dao.IAwardDao;
import com.swpu.lottery.infra.dao.IStrategyDao;
import com.swpu.lottery.infra.dao.IStrategyDetailDao;
import com.swpu.lottery.infra.po.Award;
import com.swpu.lottery.infra.po.Strategy;
import com.swpu.lottery.infra.po.StrategyDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StrategyRepository implements IStrategyRepository {
    @Autowired
    private IStrategyDao strategyDao;
    @Autowired
    private IStrategyDetailDao strategyDetailDao;
    @Autowired
    private IAwardDao awardDao;
    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {
        Strategy strategyReq=strategyDao.queryStrategy(strategyId);
        StrategyBriefVO strategy = new StrategyBriefVO();
        BeanUtils.copyProperties(strategyReq,strategy);
        List<StrategyDetail> li=strategyDetailDao.queryStrategyDetailList(strategyId);
        List<StrategyDetailBriefVO> list=new ArrayList<>();
        for(StrategyDetail strategyDetail:li){
            StrategyDetailBriefVO strategyDetailBriefVO = new StrategyDetailBriefVO();
            BeanUtils.copyProperties(strategyDetail,strategyDetailBriefVO);
            list.add(strategyDetailBriefVO);
        }
        return new StrategyRich(strategyId,strategy,list);
    }

    @Override
    public AwardBriefVO queryAwardInfo(String awardId) {
        Award award=awardDao.queryAwardInfo(awardId);
        AwardBriefVO awardReq = new AwardBriefVO();
        BeanUtils.copyProperties(award,awardReq);
        return awardReq;
    }

    @Override
    public List<String> queryNoStockStrategyAwardList(Long strategyId) {
        List<String> list=strategyDetailDao.queryNoStockStrategyAwardList(strategyId);
        return list;
    }

    @Override
    public boolean deductStock(Long strategyId, String awardId) {
        StrategyDetail req=new StrategyDetail();
        req.setStrategyId(strategyId);
        req.setAwardId(awardId);
        boolean count=strategyDetailDao.deductStock(req);
        return count==true;
    }
}

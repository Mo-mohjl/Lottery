package com.swpu.lottery.infra.dao;

import com.swpu.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
import com.swpu.lottery.infra.po.StrategyDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDetailDao {
    List<StrategyDetail> queryStrategyDetailList(Long strategyId);
    boolean deductStock(StrategyDetail req);
    List<String> queryNoStockStrategyAwardList(Long strategyId);
    void insertList(List<StrategyDetail> strategyDetails);
}

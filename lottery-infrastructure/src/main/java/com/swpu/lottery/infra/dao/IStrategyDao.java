package com.swpu.lottery.infra.dao;

import com.swpu.lottery.infra.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IStrategyDao {
    Strategy queryStrategy(Long strategyId);
    void insert(Strategy strategy);
}

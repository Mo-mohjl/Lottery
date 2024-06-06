package com.swpu.lottery.infra.dao;

import com.swpu.lottery.domain.strategy.model.vo.AwardBriefVO;
import com.swpu.lottery.infra.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAwardDao {
    Award queryAwardInfo(String awardId);
    void insertList(List<Award> awards);
}

package com.swpu.lottery.infra.dao;

import com.swpu.lottery.infra.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(Long treeId);
    RuleTree queryTreeSummaryInfo(Long treeId);
}

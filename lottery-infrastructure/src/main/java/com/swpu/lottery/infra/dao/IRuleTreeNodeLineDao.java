package com.swpu.lottery.infra.dao;

import com.swpu.lottery.infra.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRuleTreeNodeLineDao {
    List<RuleTreeNodeLine> queryRuleTreeNodeLineList(RuleTreeNodeLine ruleTreeNodeLine);
    Integer queryTreeNodeLineCount(Long treeId);
}

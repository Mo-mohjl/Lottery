package com.swpu.lottery.infra.dao;

import com.swpu.lottery.infra.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryRuleTreeNodeList(Long treeId);
    Integer queryTreeNodeCount(Long treeId);
    RuleTreeNode queryTreeRulePoint(Long treeId);
}

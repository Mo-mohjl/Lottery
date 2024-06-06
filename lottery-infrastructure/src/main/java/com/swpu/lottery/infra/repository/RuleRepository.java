package com.swpu.lottery.infra.repository;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.rule.model.aggregates.TreeRuleRich;
import com.swpu.lottery.domain.rule.model.vo.TreeNodeLineVO;
import com.swpu.lottery.domain.rule.model.vo.TreeNodeVO;
import com.swpu.lottery.domain.rule.model.vo.TreeRootVO;
import com.swpu.lottery.domain.rule.repository.IRuleRepository;
import com.swpu.lottery.infra.dao.IRuleTreeDao;
import com.swpu.lottery.infra.dao.IRuleTreeNodeDao;
import com.swpu.lottery.infra.dao.IRuleTreeNodeLineDao;
import com.swpu.lottery.infra.po.RuleTree;
import com.swpu.lottery.infra.po.RuleTreeNode;
import com.swpu.lottery.infra.po.RuleTreeNodeLine;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RuleRepository implements IRuleRepository {
    @Resource
    private IRuleTreeDao ruleTreeDao;
    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;
    @Override
    public TreeRuleRich queryTreeRuleRich(Long treeId) {
        //树根VO
        TreeRootVO treeRootVO=new TreeRootVO();
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        treeRootVO.setTreeId(ruleTree.getId());
        treeRootVO.setTreeRootNodeId(ruleTree.getTreeRootNodeId());
        treeRootVO.setTreeName(ruleTree.getTreeName());

        Map<Long, TreeNodeVO> treeNodeMap=new HashMap<>();
        List<RuleTreeNode> ruleTreeNodeList=ruleTreeNodeDao.queryRuleTreeNodeList(treeId);
        for (RuleTreeNode ruleTreeNode:ruleTreeNodeList) {
            List<TreeNodeLineVO> treeNodeLineInfoList=new ArrayList<>();
            if(Constants.NodeType.STEM.equals(ruleTreeNode.getNodeType())){
                RuleTreeNodeLine ruleTreeNodeLine=new RuleTreeNodeLine();
                ruleTreeNodeLine.setTreeId(treeId);
                ruleTreeNodeLine.setNodeIdFrom(ruleTreeNode.getId());
                List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineList(ruleTreeNodeLine);
                for(RuleTreeNodeLine ruleTreeNodeLinereq:ruleTreeNodeLineList){
                    TreeNodeLineVO treeNodeLineVO=new TreeNodeLineVO();
                    treeNodeLineVO.setNodeIdTo(ruleTreeNodeLinereq.getNodeIdTo());
                    treeNodeLineVO.setNodeIdFrom(ruleTreeNodeLinereq.getNodeIdFrom());
                    treeNodeLineVO.setRuleLimitType(ruleTreeNodeLinereq.getRuleLimitType());
                    treeNodeLineVO.setRuleLimitValue(ruleTreeNodeLinereq.getRuleLimitValue());
                    treeNodeLineInfoList.add(treeNodeLineVO);
                }
            }
            TreeNodeVO treeNodeVO=new TreeNodeVO();
            treeNodeVO.setTreeId(treeId);
            treeNodeVO.setTreeNodeId(ruleTreeNode.getId());
            treeNodeVO.setNodeType(ruleTreeNode.getNodeType());
            treeNodeVO.setNodeValue(ruleTreeNode.getNodeValue());
            treeNodeVO.setRuleDesc(ruleTreeNode.getRuleDesc());
            treeNodeVO.setRuleKey(ruleTreeNode.getRuleKey());
            treeNodeVO.setTreeNodeLineInfoList(treeNodeLineInfoList);
            treeNodeMap.put(ruleTreeNode.getId(),treeNodeVO);
        }

        TreeRuleRich treeRuleRich = new TreeRuleRich();
        treeRuleRich.setTreeRoot(treeRootVO);
        treeRuleRich.setTreeNodeMap(treeNodeMap);

        return treeRuleRich;
    }
}

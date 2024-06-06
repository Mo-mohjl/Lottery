package com.swpu.lottery.domain.rule.service.logic;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.rule.model.req.DecisionMatterReq;
import com.swpu.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

public abstract class BaseLogic implements LogicFilter {
    @Override
    public Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineInfoList){
        for(TreeNodeLineVO treeNodeLineVO:treeNodeLineInfoList){
            if(decisionLogic(matterValue,treeNodeLineVO)){
                return treeNodeLineVO.getNodeIdTo();
            }
        }
        return Constants.Global.TREE_NULL_NODE;
    }
    @Override
    public abstract String matterValue(DecisionMatterReq decisionMatter);
    private boolean decisionLogic(String matterValue, TreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case Constants.RuleLimitType.EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GT:
                return Double.parseDouble(matterValue) > Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LT:
                return Double.parseDouble(matterValue) < Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GE:
                return Double.parseDouble(matterValue) >= Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LE:
                return Double.parseDouble(matterValue) <= Double.parseDouble(nodeLine.getRuleLimitValue());
            default:
                return false;
        }
    }
}

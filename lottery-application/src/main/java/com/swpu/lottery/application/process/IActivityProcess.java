package com.swpu.lottery.application.process;

import com.swpu.lottery.application.process.req.DrawProcessReq;
import com.swpu.lottery.application.process.res.DrawProcessResult;
import com.swpu.lottery.application.process.res.RuleQuantificationCrowdResult;
import com.swpu.lottery.domain.rule.model.req.DecisionMatterReq;

public interface IActivityProcess {
    //抽奖开始参数注入uId，activityId
    DrawProcessResult doDrawProcess(DrawProcessReq req);
    RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req);
}

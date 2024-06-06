package com.swpu.lottery.domain.strategy.service.draw;

import com.swpu.lottery.domain.strategy.model.req.DrawReq;
import com.swpu.lottery.domain.strategy.model.res.DrawResult;

public interface IDrawExec {
    DrawResult doDrawExec(DrawReq req);
}

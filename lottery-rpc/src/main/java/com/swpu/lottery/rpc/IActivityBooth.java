package com.swpu.lottery.rpc;

import com.swpu.lottery.rpc.req.ActivityReq;
import com.swpu.lottery.rpc.req.DrawReq;
import com.swpu.lottery.rpc.req.QuantificationDrawReq;
import com.swpu.lottery.rpc.res.ActivityRes;
import com.swpu.lottery.rpc.res.DrawRes;

public interface IActivityBooth {
    /**
     * 指定活动抽奖
     * @param drawReq 请求参数
     * @return        抽奖结果
     */
    DrawRes doDraw(DrawReq drawReq);

    /**
     * 量化人群抽奖
     * @param quantificationDrawReq 请求参数
     * @return                      抽奖结果
     */
    DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq);
}
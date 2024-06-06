package com.swpu.lottery.domain.award.service.goods.impl;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.award.model.req.GoodsReq;
import com.swpu.lottery.domain.award.model.res.DistributionRes;
import com.swpu.lottery.domain.award.service.goods.DistributionGoods;
import com.swpu.lottery.domain.award.service.goods.IDistributionGoods;
import org.springframework.stereotype.Component;

@Component
public class PhysicalGoods extends DistributionGoods implements IDistributionGoods {
    @Override
    public DistributionRes distributionRes(GoodsReq req) {
        super.updateUserAwardState(req.getuId(), req.getOrderId(), req.getAwardId(), Constants.GrantState.COMPLETE.getCode());
        return new DistributionRes(req.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

    @Override
    public Integer getDistributionGoodsName() {
        return Constants.AwardType.PhysicalGoods.getCode();
    }
}

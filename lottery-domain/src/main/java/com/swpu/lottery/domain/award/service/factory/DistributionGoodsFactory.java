package com.swpu.lottery.domain.award.service.factory;

import com.swpu.lottery.domain.award.service.goods.IDistributionGoods;
import org.springframework.stereotype.Service;

@Service
public class DistributionGoodsFactory extends GoodsConfig{
    public IDistributionGoods getDistributionGoodsService(Integer awardType){
        return goodsMap.get(awardType);
    }
}

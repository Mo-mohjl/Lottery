package com.swpu.lottery.domain.activity.model.res;

import com.swpu.lottery.common.Result;

public class StockResult extends Result {
    /**
     * 库存 Key
     */
    private String stockKey;
    /**
     * activity 库存剩余
     */
    private Integer stockSurplusCount;
    public StockResult(String code, String info) {
        super(code, info);
    }
    public StockResult(String code, String info, String stockKey, Integer stockSurplusCount) {
        super(code, info);
        this.stockKey = stockKey;
        this.stockSurplusCount = stockSurplusCount;
    }
    public String getStockKey() {
        return stockKey;
    }
    public void setStockKey(String stockKey) {
        this.stockKey = stockKey;
    }
    public Integer getStockSurplusCount() {
        return stockSurplusCount;
    }
    public void setStockSurplusCount(Integer stockSurplusCount) {
        this.stockSurplusCount = stockSurplusCount;
    }
}
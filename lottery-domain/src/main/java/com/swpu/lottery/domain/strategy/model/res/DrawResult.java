package com.swpu.lottery.domain.strategy.model.res;

import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.strategy.model.vo.DrawAwardInfo;
import com.swpu.lottery.domain.strategy.vo.DrawAwardVO;

public class DrawResult {
    /**
     * 用户ID
     */
    private String uId;
    /**
     * 策略ID
     */
    private Long strategyId;
    /**
     * 中奖状态：0未中奖、1已中奖、2兜底奖 Constants.DrawState
     */
    private Integer drawState = Constants.DrawState.FAIL.getCode();
    /**
     * 中奖奖品信息
     */
    private DrawAwardVO drawAwardVO;
    public DrawResult() {
    }
    public DrawResult(String uId, Long strategyId, Integer drawState) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
    }
    public DrawResult(String uId, Long strategyId, Integer drawState, DrawAwardVO drawAwardVO) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
        this.drawAwardVO = drawAwardVO;
    }
    public String getuId() {
        return uId;
    }
    public void setuId(String uId) {
        this.uId = uId;
    }
    public Long getStrategyId() {
        return strategyId;
    }
    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }
    public Integer getDrawState() {
        return drawState;
    }
    public void setDrawState(Integer drawState) {
        this.drawState = drawState;
    }
    public DrawAwardVO getDrawAwardVO() {
        return drawAwardVO;
    }
    public void setDrawAwardVO(DrawAwardVO drawAwardVO) {
        this.drawAwardVO = drawAwardVO;
    }
}

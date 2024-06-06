package com.swpu.lottery.application.process.res;

import com.swpu.lottery.common.Result;
import com.swpu.lottery.domain.strategy.model.vo.DrawAwardInfo;
import com.swpu.lottery.domain.strategy.vo.DrawAwardVO;

public class DrawProcessResult extends Result {
    private DrawAwardInfo drawAwardInfo;
    private DrawAwardVO drawAwardVO;
    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardVO drawAwardVO) {
        super(code, info);
        this.drawAwardVO = drawAwardVO;
    }

    public DrawAwardVO getDrawAwardVO() {
        return drawAwardVO;
    }

    public void setDrawAwardVO(DrawAwardVO drawAwardVO) {
        this.drawAwardVO = drawAwardVO;
    }

    public DrawAwardInfo getDrawAwardInfo() {
        return drawAwardInfo;
    }

    public void setDrawAwardInfo(DrawAwardInfo drawAwardInfo) {
        this.drawAwardInfo = drawAwardInfo;
    }
}

package com.swpu.lottery.rpc.res;

import com.swpu.lottery.common.Result;
import com.swpu.lottery.rpc.dto.AwardDTO;

import java.io.Serializable;

public class DrawRes extends Result implements Serializable {
    private AwardDTO awardDTO;
    public DrawRes(String code, String info) {
        super(code, info);
    }
    public AwardDTO getAwardDTO() {
        return awardDTO;
    }
    public void setAwardDTO(AwardDTO awardDTO) {
        this.awardDTO = awardDTO;
    }
}

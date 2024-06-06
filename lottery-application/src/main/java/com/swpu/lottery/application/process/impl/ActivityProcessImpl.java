package com.swpu.lottery.application.process.impl;

import com.swpu.lottery.application.mq.producer.KafkaProducer;
import com.swpu.lottery.application.process.IActivityProcess;
import com.swpu.lottery.application.process.req.DrawProcessReq;
import com.swpu.lottery.application.process.res.DrawProcessResult;
import com.swpu.lottery.application.process.res.RuleQuantificationCrowdResult;
import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.activity.model.req.PartakeReq;
import com.swpu.lottery.domain.activity.model.res.PartakeResult;
import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.model.vo.DrawOrderVO;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import com.swpu.lottery.domain.activity.service.partake.IActivityPartake;
import com.swpu.lottery.domain.rule.model.req.DecisionMatterReq;
import com.swpu.lottery.domain.rule.model.res.EngineResult;
import com.swpu.lottery.domain.rule.service.engine.EngineFilter;
import com.swpu.lottery.domain.strategy.model.req.DrawReq;
import com.swpu.lottery.domain.strategy.model.res.DrawResult;
import com.swpu.lottery.domain.strategy.service.draw.IDrawExec;
import com.swpu.lottery.domain.strategy.vo.DrawAwardVO;
import com.swpu.lottery.domain.support.ids.IIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ActivityProcessImpl implements IActivityProcess {
    @Resource
    private IActivityPartake activityPartake;
    @Resource
    private IDrawExec drawExec;
    @Resource
    private EngineFilter engineFilter;
    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;
    @Resource
    private KafkaProducer kafkaProducer;
    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        // 1. 领取活动
        PartakeResult partakeResult = activityPartake.doPartake(new PartakeReq(req.getuId(), req.getActivityId()));
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())) {
            return new DrawProcessResult(partakeResult.getCode(), partakeResult.getInfo());
        }
        //首次领取成功发送MQ信息
        if(Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())){
            ActivityPartakeRecordVO activityPartakeRecordVO = new ActivityPartakeRecordVO();
            activityPartakeRecordVO.setuId(req.getuId());
            activityPartakeRecordVO.setActivityId(req.getActivityId());
            activityPartakeRecordVO.setStockCount(partakeResult.getStockCount());
            activityPartakeRecordVO.setStockSurplusCount(partakeResult.getStockSurplusCount());
            kafkaProducer.sendLotteryActivityPartakeRecord(activityPartakeRecordVO);
        }
        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId();
        // 2. 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq(req.getuId(), strategyId));
        if (Constants.DrawState.FAIL.getCode().equals(drawResult.getDrawState())) {
            return new DrawProcessResult(Constants.ResponseCode.LOSING_DRAW.getCode(), Constants.ResponseCode.LOSING_DRAW.getInfo());
        }
        DrawAwardVO drawAwardVO = drawResult.getDrawAwardVO();
        // 3. 结果落库
        DrawOrderVO drawOrderVO = buildDrawOrderVO(req, strategyId, takeId, drawAwardVO);
        activityPartake.recordDrawOrder(drawOrderVO);
        // 4. 发送MQ，触发发奖流程
        InvoiceVO invoiceVO = buildInvoiceVO(drawOrderVO);
        ListenableFuture<SendResult<String, Object>> future = kafkaProducer.sendLotteryInvoice(invoiceVO);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // 4.1 MQ 消息发送完成，更新数据库表 user_strategy_export.mq_state = 1
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.COMPLETE.getCode());
            }
            @Override
            public void onFailure(Throwable throwable) {
                // 4.2 MQ 消息发送失败，更新数据库表 user_strategy_export.mq_state = 2 【等待定时任务扫码补偿MQ消息】
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.FAIL.getCode());
            }
        });
        // 5. 返回结果
        return new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawAwardVO);
    }

    @Override
    public RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req) {
        EngineResult engineResult = engineFilter.process(req);
        if(!engineResult.isSuccess()){
            return new RuleQuantificationCrowdResult(Constants.ResponseCode.RULE_ERR.getCode(), Constants.ResponseCode.RULE_ERR.getInfo());
        }
        RuleQuantificationCrowdResult ruleQuantificationCrowdResult = new RuleQuantificationCrowdResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        ruleQuantificationCrowdResult.setActivityId(Long.valueOf(engineResult.getNodeValue()));
        return ruleQuantificationCrowdResult;
    }

    private DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardVO drawAwardVO) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setuId(req.getuId());
        drawOrderVO.setTakeId(takeId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardVO.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardVO.getGrantType());
        drawOrderVO.setGrantDate(drawAwardVO.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardVO.getAwardId());
        drawOrderVO.setAwardType(drawAwardVO.getAwardType());
        drawOrderVO.setAwardName(drawAwardVO.getAwardName());
        drawOrderVO.setAwardContent(drawAwardVO.getAwardContent());
        return drawOrderVO;
    }
    private InvoiceVO buildInvoiceVO(DrawOrderVO drawOrderVO){
        InvoiceVO invoiceVO = new InvoiceVO();
        BeanUtils.copyProperties(drawOrderVO,invoiceVO);
        return invoiceVO;
    }
}

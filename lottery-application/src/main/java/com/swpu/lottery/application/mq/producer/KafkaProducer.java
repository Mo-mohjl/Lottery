package com.swpu.lottery.application.mq.producer;

import com.alibaba.fastjson.JSON;
import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

@Component
public class KafkaProducer {
    private Logger logger= LoggerFactory.getLogger(KafkaProducer.class);
    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    public static final String TOPIC_INVOICE = "lottery_invoice";

    private static final String TOPIC_ACTIVITY_PARTAKE ="lottery-activity-partake";

    public ListenableFuture<SendResult<String, Object>> sendLotteryInvoice(InvoiceVO invoice){
        String objJson = JSON.toJSONString(invoice);
        logger.info("发送MQ消息 topic：{} bizId：{} message：{}", TOPIC_INVOICE, invoice.getuId(), objJson);
        return kafkaTemplate.send(TOPIC_INVOICE,objJson);
    }
    public ListenableFuture<SendResult<String,Object>> sendLotteryActivityPartakeRecord(ActivityPartakeRecordVO activityPartakeRecordVO){
        String objJson = JSON.toJSONString(activityPartakeRecordVO);
        logger.info("发送MQ消息(领取活动记录) topic：{} bizId：{} message：{}", TOPIC_ACTIVITY_PARTAKE, activityPartakeRecordVO.getuId(), objJson);
        return kafkaTemplate.send(TOPIC_ACTIVITY_PARTAKE, objJson);
    }
}

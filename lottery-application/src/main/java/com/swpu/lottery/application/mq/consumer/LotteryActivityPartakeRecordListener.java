package com.swpu.lottery.application.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.swpu.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.swpu.lottery.domain.activity.service.partake.IActivityPartake;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class LotteryActivityPartakeRecordListener {
    private Logger logger= LoggerFactory.getLogger(LotteryActivityPartakeRecordListener.class);
    @Resource
    private IActivityPartake activityPartake;
    @KafkaListener(topics = "lottery-activity-partake",groupId = "lottery")
    public void onMessage(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC)String topic){
        Optional<?> message = Optional.ofNullable(record.value());
        if(!message.isPresent()){
            return;
        }
        ActivityPartakeRecordVO activityPartakeRecordVO = JSON.parseObject((String) message.get(), ActivityPartakeRecordVO.class);
        logger.info("消费MQ消息，异步扣减活动库存 message：{}", message.get());
        activityPartake.updateActivityStock(activityPartakeRecordVO);
    }
}

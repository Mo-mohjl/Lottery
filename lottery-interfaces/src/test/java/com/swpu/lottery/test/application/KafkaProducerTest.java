package com.swpu.lottery.test.application;

import com.swpu.lottery.application.mq.producer.KafkaProducer;
import com.swpu.lottery.common.Constants;
import com.swpu.lottery.domain.activity.model.vo.InvoiceVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducerTest {
    private Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);
    @Resource
    private KafkaProducer kafkaProducer;
    @Test
    public void test_send() throws InterruptedException {
        InvoiceVO invoice = new InvoiceVO();
        invoice.setuId("Uhdgkw766120d");
        invoice.setOrderId(1444540456057864192L);
        invoice.setAwardId("3");
        invoice.setAwardType(Constants.AwardType.DESC.getCode());
        invoice.setAwardName("Code");
        invoice.setAwardContent("苹果电脑");
        invoice.setShippingAddress(null);
        invoice.setExtInfo(null);
        kafkaProducer.sendLotteryInvoice(invoice);
        while (true){
            Thread.sleep(10000);
        }
    }
}
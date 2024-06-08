package com.swpu.lottery.test.application;

import com.alibaba.fastjson.JSON;
import com.swpu.lottery.application.process.IActivityProcess;
import com.swpu.lottery.application.process.req.DrawProcessReq;
import com.swpu.lottery.application.process.res.DrawProcessResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityProcessTest {
    private Logger logger = LoggerFactory.getLogger(ActivityProcessTest.class);
    @Resource
    private IActivityProcess activityProcess;
    @Test
    public void test_doDrawProcess() {
        DrawProcessReq req = new DrawProcessReq();
        req.setuId("Uhsjdhs654548a");
        req.setActivityId(100002L);
        DrawProcessResult drawProcessResult = activityProcess.doDrawProcess(req);
        logger.info("请求入参：{}", JSON.toJSONString(req));
        logger.info("测试结果：{}", JSON.toJSONString(drawProcessResult));
    }
}

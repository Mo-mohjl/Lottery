import com.alibaba.fastjson.JSON;

import com.swpu.lottery.LotteryApplication;
import com.swpu.lottery.domain.strategy.service.draw.AbstractDrawBase;
import com.swpu.lottery.infra.dao.IActivityDao;
import com.swpu.lottery.infra.po.Activity;
import com.swpu.lottery.interfaces.facade.ActivityBooth;
import com.swpu.lottery.rpc.IActivityBooth;
import com.swpu.lottery.rpc.req.DrawReq;
import com.swpu.lottery.rpc.res.DrawRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApplication.class)
public class ApiTest {
    private Logger logger = LoggerFactory.getLogger(ApiTest.class);
    @Resource
    private IActivityDao activityDao;
    @Resource
    private AbstractDrawBase drawExec;
    @Resource
    private IActivityBooth activityBooth;
    @Test
    public void test_insert() {
        Activity activity = new Activity();
        activity.setActivityId(100004L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("仅用于插入数据测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(100);
        activity.setTakeCount(10);
        activity.setState(0);
        activity.setCreator("jiale");
        activityDao.insert(activity);
    }
    @Test
    public void test_select() {
        Activity activity = activityDao.queryActivityById(100003L);
        logger.info("测试结果：{}", JSON.toJSONString(activity));
    }
    @Test
    public void test_doDraw() {
        DrawReq drawReq = new DrawReq();
        drawReq.setuId("Uhdgkw766120d");
        drawReq.setActivityId(100001L);
        DrawRes drawRes = activityBooth.doDraw(drawReq);
        logger.info("请求参数：{}", JSON.toJSONString(drawReq));
        logger.info("测试结果：{}", JSON.toJSONString(drawRes));
    }
}
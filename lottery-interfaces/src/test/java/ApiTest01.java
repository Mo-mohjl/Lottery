import com.alibaba.fastjson.JSON;
import com.swpu.lottery.LotteryApplication;
import com.swpu.lottery.rpc.IActivityBooth;
import com.swpu.lottery.rpc.req.ActivityReq;
import com.swpu.lottery.rpc.req.DrawReq;
import com.swpu.lottery.rpc.res.ActivityRes;
import com.swpu.lottery.rpc.res.DrawRes;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryApplication.class)
public class ApiTest01 {
    private static final Logger logger= LoggerFactory.getLogger(ApiTest01.class);
    @Reference(interfaceClass = IActivityBooth.class, url = "dubbo://127.0.0.1:20880")
    private IActivityBooth activityBooth;

    @Test
    public void test_rpc() {
        DrawReq drawReq = new DrawReq();
        drawReq.setuId("Uhdgkw766120d");
        drawReq.setActivityId(100001L);
        DrawRes result = activityBooth.doDraw(drawReq);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }
}

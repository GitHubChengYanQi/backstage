package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.config.PayService;
import cn.atsoft.dasheng.uc.entity.UcPayOrder;
import cn.hutool.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PushPayInfoService {

    @Autowired
    private UcPayOrderService ucPayOrderService;

    @Autowired
    private PayService payService;

    private Logger logger = LoggerFactory.getLogger(PushPayInfoService.class);

    @Async
    public void doTask01() throws Exception {
        long start = System.currentTimeMillis();
        //使用线程的目的测试方法执行时间
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        System.out.println("执行任务1耗时：" + (end - start) + "毫秒");
//        return new AsyncResult<>(true);
    }

    @Async
    public void pushPayInfo(UcPayOrder ucPayOrder) {
        long start = System.currentTimeMillis();
        String url = ucPayOrder.getNotifyUrl();

        HashMap<String,Object> paramMap = payService.buildPushData(ucPayOrder);
        logger.warn("发送的消息："+ paramMap);
        String result = "";
        try {
            result = HttpRequest.post(url)
                    .timeout(5000)
                    .form(paramMap)
                    .execute()
                    .body();
        }catch (Exception e){
            logger.warn(url+","+e.getMessage());
            result = "";
        }


        if (result.equals("success")) {
            ucPayOrder.setStatus(2);
        }
        logger.warn("收到消息："+result);

        ucPayOrderService.updateById(ucPayOrder);
        long end = System.currentTimeMillis();
        logger.info("执行任务1耗时：" + (end - start) + "毫秒");
    }
}

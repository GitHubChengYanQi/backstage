package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


@Component

@Logger
public class AsyncMethod {

    @Autowired
    private AsynTaskService taskService;

    @Autowired
    private SkuService skuService;

    private static final Log log = LogFactory.getLog(AsyncMethod.class);

    @Async
    public void task(List<Long> ids, List<AllBomParam.SkuNumberParam> noSort, AllBomParam param,String type) {

        /**
         *调用组合方法
         */
        List<AllBomParam.SkuNumberParam> skuNumberParams;


        List<List<AllBomParam.SkuNumberParam>> allSkus = new ArrayList<>();
        if (ToolUtil.isEmpty(ids)) {    //没有组合
            skuNumberParams = new ArrayList<>(noSort);
            allSkus.add(skuNumberParams);
        } else {                         //有组合
            List<List<Long>> lists = skuIdsList(ids);
            for (List<Long> list : lists) {
                skuNumberParams = new ArrayList<>(noSort);
                for (Long aLong : list) {
                    for (AllBomParam.SkuNumberParam numberParam : param.getSkuIds()) {
                        if (aLong.equals(numberParam.getSkuId())) {
                            skuNumberParams.add(numberParam);
                            break;
                        }
                    }
                }
                allSkus.add(skuNumberParams);
            }
        }


        log.info("async task is start");

        AsynTask asynTask = new AsynTask();
        asynTask.setAllCount(allSkus.size());
        asynTask.setType(type);
        asynTask.setStatus(0);
        taskService.save(asynTask);

        /**
         *  调用bom方法
         */
        boolean t = true;

        AllBomResult allBomResult = new AllBomResult();

        List<AnalysisResult> owes = new ArrayList<>();
        int i = 0;
        for (List<AllBomParam.SkuNumberParam> skus : allSkus) {
            i++;
            AllBom allBom = new AllBom();
            allBom.start(skus, t);
            AllBomResult bom = allBom.getResult();

            List<BomOrder> result = allBomResult.getResult();
            if (ToolUtil.isEmpty(result)) {
                result = new ArrayList<>();
            }
            result.addAll(bom.getResult());
            allBomResult.setResult(result);     //够生产

            owes.addAll(bom.getOwe());
            t = false;   //控制缺料集合
            asynTask.setCount(i);   //修改任务状态
            taskService.updateById(asynTask);
        }
        /**
         * 数据添加到 队列里
         */


        allBomResult.setOwe(owes);

        List<Long> skuIds = new ArrayList<>();
        for (AllBomParam.SkuNumberParam skuId : param.getSkuIds()) {
            skuIds.add(skuId.getSkuId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<AllBomResult.View> views = new ArrayList<>();
        for (AllBomParam.SkuNumberParam skuId : param.getSkuIds()) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(skuId.getSkuId())) {
                    AllBomResult.View view = new AllBomResult.View();
                    view.setFixed(skuId.getFixed());
                    if (ToolUtil.isEmpty(skuResult.getSpecifications())) {
                        view.setName(skuResult.getSpuResult().getName() + " / " + skuResult.getSkuName());
                    } else {
                        view.setName(skuResult.getSpuResult().getName() + " / " + skuResult.getSkuName() + " / " + skuResult.getSpecifications());
                    }
                    view.setNum(skuId.getNum());
                    view.setSkuId(skuId.getSkuId());
                    views.add(view);
                    break;
                }
            }
        }

        allBomResult.setView(views);


        asynTask.setContent(JSON.toJSONString(allBomResult));
        asynTask.setStatus(99);
        taskService.updateById(asynTask);

    }


    public static List<List<Long>> skuIdsList(List<Long> skuIds) {
        List<List<Long>> skuIdsCell = new ArrayList<>();
        skuPartsMakeUp(skuIds, skuIds.size(), 0, skuIdsCell);
        return skuIdsCell;
    }

    /**
     * 排列组合sku
     */
    public static Stack<Long> stack = new Stack<Long>();

    public static void skuPartsMakeUp(List<Long> skuIds, int count, int now, List<List<Long>> skuIdsCell) {
        if (now == count) {
            List<Long> stacks = new ArrayList<Long>(stack);
            skuIdsCell.add(stacks);

            return;
        }
        for (int i = 0; i < skuIds.size(); i++) {
            if (!stack.contains(skuIds.get(i))) {
                stack.add(skuIds.get(i));
                skuPartsMakeUp(skuIds, count, now + 1, skuIdsCell);
                stack.pop();
            }
        }
    }


}

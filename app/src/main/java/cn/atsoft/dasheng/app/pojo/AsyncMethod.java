package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
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

    private static final Log log = LogFactory.getLog(AsyncMethod.class);

    @Async
    public void task(List<Long> ids, List<AllBomParam.SkuNumberParam> noSort, AllBomParam param) {

        /**
         *调用组合方法
         */
        List<AllBomParam.SkuNumberParam> skuNumberParams;


        List<List<AllBomParam.SkuNumberParam>> allSkus = new ArrayList<>();
        if (ToolUtil.isEmpty(ids)) {    //没有组合
            skuNumberParams = new ArrayList<>(noSort);
            allSkus.add(skuNumberParams);
        }else {                         //有组合
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
        asynTask.setType("物料分析");
        asynTask.setStatus(0);
        taskService.save(asynTask);

        /**
         *  调用bom方法
         */

        AllBomResult allBomResult = new AllBomResult();
        List<BomOrder> results = new ArrayList<>();
        List<SkuResult> owes = new ArrayList<>();
        int i = 0;
        for (List<AllBomParam.SkuNumberParam> skus : allSkus) {
            i++;
            AllBom allBom = new AllBom();
            allBom.start(skus);
            AllBomResult bom = allBom.getResult();
            results.addAll(bom.getResult());
            owes.addAll(bom.getOwe());

            asynTask.setCount(i);   //修改任务状态
            taskService.updateById(asynTask);
        }
        allBomResult.setResult(results);
        allBomResult.setOwe(owes);

        asynTask.setContent(JSON.toJSONString(allBomResult));
        asynTask.setStatus(99);
        taskService.updateById(asynTask);

    }

    private List<List<Long>> skuIdsList(List<Long> skuIds) {
        List<List<Long>> skuIdsCell = new ArrayList<>();
        this.skuPartsMakeUp(skuIds, skuIds.size(), 0, skuIdsCell);
        return skuIdsCell;
    }

    /**
     * 排列组合sku
     */
    public static Stack<Long> stack = new Stack<Long>();

    private void skuPartsMakeUp(List<Long> skuIds, int count, int now, List<List<Long>> skuIdsCell) {
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

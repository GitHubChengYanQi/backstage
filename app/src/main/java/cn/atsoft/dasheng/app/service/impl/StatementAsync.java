package cn.atsoft.dasheng.app.service.impl;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.pojo.StockStatement;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.asyn.entity.AsynTask;
import cn.atsoft.dasheng.asyn.pojo.SkuAnalyse;
import cn.atsoft.dasheng.asyn.service.AsynTaskService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatementAsync {

    @Autowired
    private AsynTaskService asynTaskService;
    @Autowired
    private StockDetailsService stockDetailsService;


    /**
     * 异步计算库存报表
     */
    @Async
    public void startAnalyse() {
        asynTaskService.remove(new QueryWrapper<AsynTask>() {{
            eq("type", "库存报表");
        }});

        List<StockDetails> stockDetails = stockDetailsService.query().eq("display", 1).list();
        List<StockDetailsResult> detailsResults = BeanUtil.copyToList(stockDetails, StockDetailsResult.class);

        List<Long> skuIds = new ArrayList<>();
        for (StockDetailsResult detailsResult : detailsResults) {
            skuIds.add(detailsResult.getSkuId());
        }
        List<SkuAnalyse> skuAnalyseList = asynTaskService.skuAnalyses(skuIds);

        for (StockDetailsResult detailsResult : detailsResults) {
            for (SkuAnalyse skuAnalyse : skuAnalyseList) {
                if (detailsResult.getSkuId().equals(skuAnalyse.getSkuId())) {
                    detailsResult.setSpuClassName(skuAnalyse.getClassName());
                    detailsResult.setSpuClassificationId(skuAnalyse.getSpuClassId());
                    detailsResult.setSpuClassNum(skuAnalyse.getNumber());
                    break;
                }
            }
        }

        /**
         * 时间比对
         */
        Date nowDate = new Date();
        for (StockDetailsResult detailsResult : detailsResults) {
            boolean b = false;
            int amount = 30;
            b = isMonth(detailsResult.getCreateTime(), nowDate, amount);
            if (b) {         //满足一个月内
                detailsResult.setMonth(amount + "天");
            } else {        //不满足一个月
                amount = 90;
                b = isMonth(detailsResult.getCreateTime(), nowDate, amount);
                if (b) {    //满足3个月内
                    detailsResult.setMonth(amount + "天");
                } else {    //不满足3个月内
                    amount = 180;
                    b = isMonth(detailsResult.getCreateTime(), nowDate, amount);
                    if (b) {    //满足6个月内
                        detailsResult.setMonth(amount + "天");
                    } else {    //不满足6个月内
                        detailsResult.setMonth("长期呆滞");
                    }
                }
            }
        }


        /**
         * 数据组合
         */
        List<StockDetailsResult> totalList = new ArrayList<>();
        detailsResults.parallelStream().collect(Collectors.groupingBy(item -> item.getMonth() + item.getSpuClassName(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetailsResult() {{
                        setNumber(a.getNumber() + b.getNumber());
                        setMonth(a.getMonth());
                        setSpuClassName(a.getSpuClassName());
                        setSkuIds(new HashSet<Long>() {{
                            add(a.getSkuId());
                        }});
                    }}).ifPresent(totalList::add);
                }
        );


        List<StockStatement> stockStatements = new ArrayList<>();
        for (StockDetailsResult stockDetailsResult : totalList) {
            if (ToolUtil.isEmpty(stockDetailsResult.getSkuIds())) {
                Set<Long> newSkuIds = new HashSet<>();
                stockDetailsResult.setSkuIds(newSkuIds);
            }
            StockStatement stockStatement = new StockStatement();
            switch (stockDetailsResult.getMonth()) {
                case "30天":
                    stockStatement.setMonth("1个月内");
                    break;
                case "90天":
                    stockStatement.setMonth("3个月内");
                    break;
                case "180天":
                    stockStatement.setMonth("6个月内");
                    break;
                default:
                    stockStatement.setMonth(stockDetailsResult.getMonth());
            }

            stockStatement.setName(stockDetailsResult.getSpuClassName());
            stockStatement.setValue(stockDetailsResult.getNumber());
            stockStatement.setNum((long) stockDetailsResult.getSkuIds().size());
            stockStatements.add(stockStatement);
        }
        AsynTask asynTask = new AsynTask();
        asynTask.setType("库存报表");
        asynTask.setAllCount(1);
        asynTask.setCount(1);
        asynTask.setContent(JSON.toJSONString(stockStatements));
        asynTaskService.save(asynTask);

    }

    private boolean isMonth(Date date, Date nowDate, int amount) {
        Calendar oneC = Calendar.getInstance();
        oneC.setTimeInMillis(date.getTime());
        oneC.add(Calendar.DATE, amount);
        Date amountMonth = new Date(oneC.getTimeInMillis());
        System.err.println("入库时间" + new DateTime(date));
        System.err.println("入库时间" + amount + "天后的时间" + new DateTime(amountMonth));
        System.err.println("当前时间" + new DateTime(nowDate));
        return amountMonth.after(nowDate);
    }

}

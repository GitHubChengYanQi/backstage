package cn.atsoft.dasheng.app.pojo;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;

import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.AllBomService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.form.service.StepsService;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;


public class AllBom {

    private final PartsService partsService = SpringContextHolder.getBean(PartsService.class);


    private final ErpPartsDetailService detailService = SpringContextHolder.getBean(ErpPartsDetailService.class);


    private final StockDetailsService stockDetailsService = SpringContextHolder.getBean(StockDetailsService.class);


    private final SkuService skuService = SpringContextHolder.getBean(SkuService.class);


    private final Map<Long, Long> notEnough = new HashMap<>();  //缺料数

    private final Map<Long, Map<String, Map<Long, Object>>> Bom = new HashMap<>();

    private final Map<Long, Integer> skuList = new HashMap<>();  //生产一套所需要的物料和数量

    private final Map<Long, Long> stockNumber = new HashMap<>();  //库存数量

    private final Map<Long, Long> mix = new LinkedHashMap<>();   //最少可生产数量;


    public AllBomResult getResult() {
        AllBomResult allBomResult = new AllBomResult();
        List<Object> canProduce = new ArrayList<>();
        BomOrder bomOrder = new BomOrder();

        /**
         * 可生产数量排序
         */
        List<Map.Entry<Long, Long>> list = new ArrayList<>(mix.entrySet());
        list.sort(new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {

                //按照value值，从大到小排序
                return Math.toIntExact(o2.getValue() - o1.getValue());

            }
        });

        int num = 0;
        List<Long> skus = new ArrayList<>();
        for (Map.Entry<Long, Long> longLongEntry : list) {
            Long skuId = longLongEntry.getKey();
            skus.add(skuId);
        }
        List<SkuResult> results = skuService.formatSkuResult(skus);
        for (Map.Entry<Long, Long> longLongEntry : list) {
            for (SkuResult skuResult : results) {
                if (skuResult.getSkuId().equals(longLongEntry.getKey())) {
                    AnalysisResult analysisResult = new AnalysisResult();
                    analysisResult.setSkuName(skuResult.getSkuName());
                    analysisResult.setSpuName(skuResult.getSpuResult().getName());
                    analysisResult.setProduceMix(longLongEntry.getValue());
                    analysisResult.setSpecifications(skuResult.getSpecifications());
                    analysisResult.setStrand(skuResult.getStandard());
                    analysisResult.setSkuId(skuResult.getSkuId());
                    canProduce.add(analysisResult);
                    num = Math.toIntExact(num + longLongEntry.getValue());
                    break;
                }
            }
        }
        bomOrder.setResult(canProduce);
        bomOrder.setNum(num);

        /**
         * 缺料数量排序
         */
        List<AnalysisResult> analysisResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(notEnough)) {
            List<Map.Entry<Long, Long>> notEnoughList = new ArrayList<>(notEnough.entrySet());
            notEnoughList.sort(new Comparator<Map.Entry<Long, Long>>() {
                @Override
                public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {

                    //按照value值，从大到小排序
                    return Math.toIntExact(o2.getValue() - o1.getValue());

                }
            });

            Set<Long> ids = notEnough.keySet();
            List<Long> skuIds = new ArrayList<>(ids);
            List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);


            for (Map.Entry<Long, Long> longLongEntry : notEnoughList) {
                for (SkuResult skuResult : skuResults) {
                    if (longLongEntry.getKey().equals(skuResult.getSkuId())) {
                        AnalysisResult analysisResult = new AnalysisResult();
                        Long number = notEnough.get(skuResult.getSkuId());
                        analysisResult.setSkuId(skuResult.getSkuId());
                        analysisResult.setSkuName(skuResult.getSkuName());
                        analysisResult.setSpuName(skuResult.getSpuResult().getName());
                        analysisResult.setLackNumber(number);
                        analysisResult.setStrand(skuResult.getStandard());
                        analysisResult.setSpecifications(skuResult.getSpecifications());
                        analysisResults.add(analysisResult);
                        break;
                    }
                }
            }

        }


        allBomResult.setResult(new ArrayList<BomOrder>() {{
            add(bomOrder);
        }});
        allBomResult.setOwe(analysisResults);
        return allBomResult;
    }


    public void start(List<AllBomParam.SkuNumberParam> params, boolean mixAdd) {

        /**
         * 获取bom
         */
        for (AllBomParam.SkuNumberParam param : params) {
            getBom(param.getSkuId(), 1, 0);
        }

        /**
         * 计算库存
         */
        getNumber();

        /**
         *  开始计算
         */
        for (AllBomParam.SkuNumberParam param : params) {
            getMix(param.getSkuId(), param.getNum(), mixAdd);
        }

    }

    /**
     * 1=>[
     * {2=>{},3=>{},4=>{}},
     * {5=>{},6=>{},7=>{}}
     * ]
     */
    public Map<Long, Object> getBom(Long skuId, int number, double selfNum) {

        Map<Long, Object> tmp = new HashMap<>();


        Parts one = partsService.query().eq("sku_id", skuId).eq("status", 99).eq("type", 1).one();


        if (ToolUtil.isNotEmpty(one)) {
            List<ErpPartsDetail> details = detailService.query().eq("parts_id", one.getPartsId()).list();
            /**
             * 循环bom 作为 0 下标元素
             */
            Map<Long, Object> tmp2 = new HashMap<>();
            for (ErpPartsDetail detail : details) {
                tmp2.put(detail.getSkuId(), detail);
            }
            /**
             * 递归 取下级 作 1下表元素
             */

            List<Map<Long, Object>> list = new ArrayList<>();
            for (ErpPartsDetail erpPartsDetail : details) {
                list.add(this.getBom(erpPartsDetail.getSkuId(), (int) (erpPartsDetail.getNumber() * number), erpPartsDetail.getNumber()));
            }

            // 循环相加
            Map<Long, Object> map = new HashMap<>();
            for (Map<Long, Object> objectMap : list) {

                for (Long aLong : objectMap.keySet()) {
                    SkuNumber sn = (SkuNumber) objectMap.get(aLong);
                    if (ToolUtil.isNotEmpty(map.get(aLong))) {
                        SkuNumber sk = (SkuNumber) map.get(aLong);
                        sk.setNum(sk.getNum() + sn.getNum());
                        map.put(aLong, sk);
                    } else {
                        map.put(aLong, sn);
                    }
                }
            }
            this.Bom.put(skuId, new HashMap<String, Map<Long, Object>>() {{
                put("lastChild", map);
                put("child", tmp2);
            }});

            // 循环相乘，之后相加，放到tmp里


            for (Long aLong : map.keySet()) {
                SkuNumber o = (SkuNumber) map.get(aLong);
                SkuNumber skuNumber = new SkuNumber();
                ToolUtil.copyProperties(o, skuNumber);
                skuNumber.setNum((int) (o.getNum() * selfNum));
                tmp.put(aLong, skuNumber);
            }


        } else {
            Integer num = 0;
            if (ToolUtil.isNotEmpty(skuList.get(skuId))) {
                num = skuList.get(skuId);
            }
            skuList.put(skuId, number + num);
            SkuNumber skuNumber = new SkuNumber();
            skuNumber.setNum((int) selfNum);
            skuNumber.setSkuId(skuId);
            tmp.put(skuId, skuNumber);
        }
        return tmp;

    }


    public void getNumber() {

        /**
         * 查询库存
         */
        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*", "sum(number) AS num").groupBy("sku_id");
        List<StockDetails> details = stockDetailsService.list(queryWrapper);
        for (StockDetails detail : details) {
            stockNumber.put(detail.getSkuId(), detail.getNum());
        }


        /**
         * 追加下级库存
         */
        Set<Long> longs = Bom.keySet();
        for (Long bomSkuId : longs) {
            Map<String, Map<Long, Object>> map = Bom.get(bomSkuId);
            Long number = stockNumber.get(bomSkuId);

            if (ToolUtil.isNotEmpty(number)) {
                Map<Long, Object> child = map.get("lastChild");
                for (Long skuId : child.keySet()) {
                    Long noNum = stockNumber.get(skuId);   //库存没有 添加
                    if (ToolUtil.isEmpty(noNum)) {
                        stockNumber.put(skuId, 0L);
                    }

                    SkuNumber skuNumber = (SkuNumber) child.get(skuId);
                    long l = number * skuNumber.getNum();
                    Long lastNum = stockNumber.get(skuId);
                    if (ToolUtil.isNotEmpty(lastNum)) {
                        stockNumber.put(skuId, lastNum + l);
                    }
                }
            } else {
                stockNumber.put(bomSkuId, 0L);
                Map<Long, Object> child = map.get("lastChild");
                for (Long id : child.keySet()) {
                    if (ToolUtil.isEmpty(stockNumber.get(id))) {
                        stockNumber.put(id, 0L);
                    }

                }
            }
        }


    }

    /**
     * 计算最小可生产
     *
     * @param skuId
     * @param number
     */
    public void getMix(Long skuId, Long number, boolean mixAdd) {

        Map<String, Map<Long, Object>> bomMap = Bom.get(skuId);
        List<Long> noEnough = new ArrayList<>();

        Map<Long, Object> lastChild = bomMap.get("lastChild");

        for (Long id : lastChild.keySet()) {
            Long stockNum = stockNumber.get(id);
            SkuNumber skuNumber = (SkuNumber) lastChild.get(id);
            if (ToolUtil.isNotEmpty(skuNumber)) {
                if (stockNum == 0) {
                    noEnough.add(0L);
                } else {
                    long mix = stockNum / skuNumber.getNum();
                    noEnough.add(mix);
                }
            }
        }
        Long min = Collections.min(noEnough);
        if (min > number) {
            min = number;
        }
        mix.put(skuId, min);


        /**
         * 如果这个物料不够生产  不更新库
         */
        if (min > 0) {
            for (Long id : lastChild.keySet()) {
                SkuNumber skuNumber = (SkuNumber) lastChild.get(id);
                long num = skuNumber.getNum() * min;
                Long stockNum = stockNumber.get(id);
                stockNumber.put(id, stockNum - num);
            }
        } else if (min == 0) {    //缺料
            for (Long id : lastChild.keySet()) {
                Long stockNum = stockNumber.get(id);
                SkuNumber skuNumber = (SkuNumber) lastChild.get(id);
                long num = skuNumber.getNum() * number;
                if (mixAdd && (num - stockNum > 0)) {
                    this.notEnough.put(id, num - stockNum);
                }
            }
        }
    }

}



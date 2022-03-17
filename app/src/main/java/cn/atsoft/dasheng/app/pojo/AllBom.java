package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.AllBomService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
public class AllBom {

    @Autowired
    @JSONField(serialize = false)
    private AllBomService allBomService;

    @Autowired
    @JSONField(serialize = false)
    private PartsService partsService;

    @Autowired
    @JSONField(serialize = false)
    private ErpPartsDetailService detailService;

    @Autowired
    @JSONField(serialize = false)
    private StockDetailsService stockDetailsService;

    @Autowired
    @JSONField(serialize = false)
    private ActivitiProcessService processService;

    @Autowired
    @JSONField(serialize = false)
    private StepsService stepsService;

    @Autowired
    @JSONField(serialize = false)
    private ActivitiSetpSetDetailService setDetailService;

    @Autowired
    @JSONField(serialize = false)
    private ActivitiSetpSetService setService;

    private Map<Long, Integer> notEnough = new HashMap<>();  //缺料数

    private Map<Long, Integer> enough = new HashMap<>(); //够料

    private Map<Long, ArrayList<Map<Long, Object>>> Bom = new HashMap<>();

    private Map<Long, Integer> skuList = new HashMap<>();  //生产一套所需要的物料和数量

    private Map<Long, Long> stockNumber = new HashMap<>();  //库存数量

    private Map<Long, Integer> shipSku = new HashMap<>();  //所有子工艺sku

    private Integer mix;   //最少可生产数量;


    public void start() {
        for (Map.Entry<Long, Integer> integerEntry : shipSku.entrySet()) {
            getBom(integerEntry.getKey(), integerEntry.getValue());
        }
    }


    public Map<Long, Integer> getAllShip(Long processId, int num) {
        Map<Long, Integer> map = new HashMap<>();
        ActivitiStepsResult stepsResult = stepsService.detail(processId);//树形结构
        if (ToolUtil.isNotEmpty(stepsResult.getProcess())) {   //当前工艺
            ActivitiProcess process = processService.getById(stepsResult.getProcess().getProcessId());
            map.put(process.getFormId(), num);
        }
        this.loopGetProcessList(stepsResult, num, map);
        this.shipSku = map;
        return map;
    }


    private void loopGetProcessList(ActivitiStepsResult stepsResult, int num, Map<Long, Integer> shipSku) {
        if (ToolUtil.isNotEmpty(stepsResult)) {
            switch (stepsResult.getStepType()) {
                case "ship":
                    ActivitiSetpSetDetail setDetail = setDetailService.query().eq("setps_id", stepsResult.getSetpsId()).one();
                    if (ToolUtil.isNotEmpty(setDetail)) {
                        num = num * setDetail.getNum();
                        shipSku.put(setDetail.getSkuId(), setDetail.getNum());
                    }
                    shipSku.putAll(getAllShip(stepsResult.getProcess().getProcessId(), num));
                    break;
                case "shipStart":
                case "setp":
                    loopGetProcessList(stepsResult.getChildNode(), num, shipSku);
                    break;
                case "route":
                    for (ActivitiStepsResult activitiStepsResult : stepsResult.getConditionNodeList()) {
                        loopGetProcessList(activitiStepsResult, num, shipSku);
                    }
                    break;
            }
        }

    }


    /**
     * 1=>[
     * {2=>{},3=>{},4=>{}},
     * {5=>{},6=>{},7=>{}}
     * ]
     */

    public Map<Long, Object> getBom(Long skuId, int number) {

        Map<Long, Object> tmp = new HashMap<>();

        //没有工艺路线
        Parts one = partsService.query().eq("sku_id", skuId).eq("status", 99).one();


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

            for (ErpPartsDetail erpPartsDetail : details) {
                tmp.putAll(this.getBom(erpPartsDetail.getSkuId(), erpPartsDetail.getNumber() * number));

            }

            this.Bom.put(skuId, new ArrayList<Map<Long, Object>>() {{
                if (ToolUtil.isNotEmpty(tmp2)) {
                    add(tmp2);
                }
                if (ToolUtil.isNotEmpty(tmp)) {
                    add(tmp);
                }
            }});
        } else {
            Integer num = 0;
            if (ToolUtil.isNotEmpty(skuList.get(skuId))) {
                num = skuList.get(skuId);
            }
            skuList.put(skuId, number + num);

        }
        return tmp;

    }


    public void getNumber(int number) {

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
         * 所有子工艺的最下级
         */

        Map<Long, Integer> allBomNumber = new HashMap<>();

        for (Map.Entry<Long, ArrayList<Map<Long, Object>>> bom : Bom.entrySet()) {
            ArrayList<Map<Long, Object>> bomValue = bom.getValue();
            for (Map<Long, Object> objectMap : bomValue) {
                for (Map.Entry<Long, Object> longObjectEntry : objectMap.entrySet()) {
                    ErpPartsDetail detail = (ErpPartsDetail) longObjectEntry.getValue();
                    allBomNumber.put(detail.getSkuId(), detail.getNumber() * number);
                }
            }
        }

        Set<Long> keySet = allBomNumber.keySet();
        for (Long aLong : keySet) {
            Long stockNum = stockNumber.get(aLong);
            if (ToolUtil.isNotEmpty(stockNum)) {
                stockNumber.put(aLong, allBomNumber.get(aLong) + stockNumber.get(aLong));
            } else {
                stockNumber.put(aLong, Long.valueOf(allBomNumber.get(aLong)));
            }
        }


    }

    public void getMix(int number) {
        List<Integer> mix = new ArrayList<>();
        List<Integer> max = new ArrayList<>();
        for (Long aLong : skuList.keySet()) {
            Integer skuNumber = skuList.get(aLong);
            skuNumber = skuNumber * number;
            Long stockSkuNumber = stockNumber.get(aLong);
            if (ToolUtil.isEmpty(stockSkuNumber)) {    //库存没有 直接缺料
                notEnough.put(aLong, skuNumber);
            } else if (skuNumber - stockSkuNumber > 0) {  //缺料
//                int need = Math.toIntExact(skuNumber - stockSkuNumber);
                int i = Math.toIntExact((skuNumber / stockSkuNumber));//不能生产数量
                mix.add(i);
                notEnough.put(aLong, Math.toIntExact((skuNumber - stockSkuNumber)));
            } else if (stockSkuNumber - skuNumber >= 0) {
                enough.put(aLong, Math.toIntExact(stockSkuNumber - (long) skuNumber));
                int l = Math.toIntExact(stockSkuNumber / skuNumber);
                max.add(l);
            }
        }
        if (ToolUtil.isNotEmpty(mix)) {
            this.mix = Collections.min(mix);    //最少可生产；
        }
        if (ToolUtil.isEmpty(mix) && ToolUtil.isNotEmpty(max)) {
            this.mix = Collections.min(max);
        }
    }
}



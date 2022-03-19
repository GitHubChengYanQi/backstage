package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
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
import io.swagger.models.auth.In;
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
    private SkuService skuService;

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
                        shipSku.put(setDetail.getSkuId(), num);
                    }
                    shipSku.putAll(getAllShip(stepsResult.getProcess().getProcessId(), num));
                    break;
                case "shipStart":
                    loopGetProcessList(stepsResult.getChildNode(), num, shipSku);
                    break;
                case "setp":
                    ActivitiSetpSet setpSet = setService.query().eq("setps_id", stepsResult.getSetpsId()).eq("production_type", "out").one();  //投入和产出不相同
                    if (ToolUtil.isNotEmpty(setpSet)) {
                        List<ActivitiSetpSetDetail> setDetails = setDetailService.query().eq("setps_id", stepsResult.getSetpsId()).eq("type", "out").list();  //产出
                        for (ActivitiSetpSetDetail detail : setDetails) {
                            if (ToolUtil.isNotEmpty(skuList.get(detail.getSkuId()))) {
                                Integer integer = skuList.get(detail.getSkuId());
                                skuList.put(detail.getSkuId(), (detail.getNum() * num) + integer);
                            } else {
                                skuList.put(detail.getSkuId(), detail.getNum() * num);
                            }
                        }
                    }
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

            for (ErpPartsDetail erpPartsDetail : details) {
                tmp.putAll(this.getBom(erpPartsDetail.getSkuId(), erpPartsDetail.getNumber() * number));
            }

            this.Bom.put(skuId, new ArrayList<Map<Long, Object>>() {{
                add(tmp2);
                add(tmp);
            }});
        } else {
            Integer num = 0;
            if (ToolUtil.isNotEmpty(skuList.get(skuId))) {
                num = skuList.get(skuId);
            }
            skuList.put(skuId, number + num);

            SkuNumber skuNumber = new SkuNumber();
            skuNumber.setNum(number);
            skuNumber.setSkuId(skuId);
            tmp.put(skuId, skuNumber);
        }
        return tmp;

    }


    public void getNumber(Long skuId) {

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
        for (Long aLong : Bom.keySet()) {
            ArrayList<Map<Long, Object>> maps = Bom.get(aLong);
            if (!aLong.equals(skuId)) {   //不能是最顶级

                Map<Long, Object> map = maps.get(0);  //中间级
                Map<Long, Object> childMap = maps.get(1); //最下级

                if (ToolUtil.isNotEmpty(childMap)) {
                    for (Long id : map.keySet()) {    //中间级循环

                        Long number = stockNumber.get(aLong);
                        if (ToolUtil.isNotEmpty(number)) {   //库存没有中间级 不追加库存

                            ErpPartsDetail detail = (ErpPartsDetail) map.get(id);

                            SkuNumber skuNumber = (SkuNumber) childMap.get(id);
                            long l = detail.getNumber() * number;

                            if (ToolUtil.isNotEmpty(this.stockNumber.get(skuNumber.getSkuId()))) {
                                stockNumber.put(skuNumber.getSkuId(), stockNumber.get(skuNumber.getSkuId()) + l);
                            } else {
                                stockNumber.put(skuNumber.getSkuId(), l);
                            }
                        }
                    }
                }
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
                mix.add(0);
            } else if (skuNumber - stockSkuNumber > 0) {           //缺料
                int j = (int) ((skuNumber - stockSkuNumber) / (skuNumber / number));//不能生产数量
                long l = (skuNumber - stockSkuNumber) % (skuNumber / number);  //取余数
                if (ToolUtil.isNotEmpty(l) && l > 0) {
                    j = j + 1;
                }
                mix.add(number - j);
                notEnough.put(aLong, Math.toIntExact((skuNumber - stockSkuNumber)));
            } else if (stockSkuNumber - skuNumber >= 0) {         //够料
                enough.put(aLong, Math.toIntExact(stockSkuNumber - (long) skuNumber));

                int j = (int) ((skuNumber - stockSkuNumber) / (skuNumber / number));//不能生产数量
                long l = (skuNumber - stockSkuNumber) % (skuNumber / number);  //取余数
                if (ToolUtil.isNotEmpty(l) && l > 0) {
                    j = j + 1;
                }
                max.add(number - j);
            }
        }
        if (ToolUtil.isNotEmpty(mix)) {
            this.mix = Collections.min(mix);    //最少可生产；
        }
        if (ToolUtil.isEmpty(mix) && ToolUtil.isNotEmpty(max)) {
            this.mix = Collections.min(max);
        }
        if (this.mix >= number) {
            this.mix = number;
        }
    }
}



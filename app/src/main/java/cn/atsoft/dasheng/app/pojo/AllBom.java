package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.AllBomService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.StepsType;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.html.HTMLParagraphElement;
import sun.util.resources.cldr.mg.LocaleNames_mg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private Map<Long, ArrayList<Map<Long, Object>>> Bom = new HashMap<>();

    private Map<Long, Integer> skuList = new HashMap<>();

    private Map<Long, Long> stockNumber = new HashMap<>();

    private Map<Long, Integer> shipSku = new HashMap<>();   //所有子工艺sku


    public void start() {
        for (Map.Entry<Long, Integer> integerEntry : shipSku.entrySet()) {
            getBom(integerEntry.getKey(), integerEntry.getValue());
        }
    }


    public Map<Long, Integer> getAllShip(Long processId, int num) {
        Map<Long, Integer> map = new HashMap<>();
        ActivitiStepsResult stepsResult = stepsService.detail(processId);//树形结构
        this.loopGetProcessList(stepsResult, 1, map);
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
                    } else {
                        shipSku.put(stepsResult.getActivitiProcessResult().getFormId(), 1);
                    }
                    shipSku.putAll(getAllShip(stepsResult.getActivitiProcessResult().getProcessId(), num));
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


    public void getNumber() {

        List<Long> skuIds = new ArrayList<>();
        for (Map.Entry<Long, ArrayList<Map<Long, Object>>> longArrayListEntry : Bom.entrySet()) {
            skuIds.add(longArrayListEntry.getKey());
        }
        /**
         * 查询库存
         */

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*", "sum(number) AS num").in("sku_id", skuIds).groupBy("sku_id");
        List<StockDetails> details = skuIds.size() == 0 ? new ArrayList<>() : stockDetailsService.list(queryWrapper);

        for (StockDetails detail : details) {
            for (Long skuId : skuIds) {
                if (skuId.equals(detail.getSkuId())) {
                    stockNumber.put(detail.getSkuId(), detail.getNum());
                }
            }
        }

    }

}

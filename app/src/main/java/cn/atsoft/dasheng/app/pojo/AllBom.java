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

    private List<Long> skuIds = new ArrayList<>();   //所有


    public void start(Long processId) {
        for (Long skuId : skuIds) {
            getBom(skuId, 1);
        }
    }


    public List<Long> getAllShip(Long processId) {
        List<Long> allSku = new ArrayList<>();
        ActivitiStepsResult stepsResult = stepsService.detail(processId);//树形结构
        this.loopGetProcessList(stepsResult, allSku);
        this.skuIds = allSku;
        return allSku;
    }


    private void loopGetProcessList(ActivitiStepsResult stepsResult, List<Long> skuIds) {
        if (ToolUtil.isNotEmpty(stepsResult)) {
            switch (stepsResult.getStepType()) {
                case "ship":
                    skuIds.add(stepsResult.getActivitiProcessResult().getFormId());
                    skuIds.addAll(getAllShip(stepsResult.getActivitiProcessResult().getProcessId()));
//                stepsResult.get
                    break;
                case "shipStart":
                case "setp":
                    loopGetProcessList(stepsResult.getChildNode(), skuIds);
                    break;
                case "route":
                    for (ActivitiStepsResult activitiStepsResult : stepsResult.getConditionNodeList()) {
                        loopGetProcessList(activitiStepsResult, skuIds);
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
                add(tmp2);
                add(tmp);
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

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

    private Map<Long, Map<String, Map<Long, Object>>> Bom = new HashMap<>();

    private Map<Long, Integer> skuList = new HashMap<>();  //生产一套所需要的物料和数量

    private Map<Long, Long> stockNumber = new HashMap<>();  //库存数量

    private Map<Long, Long> mix;   //最少可生产数量;


    /**
     * 1=>[
     * {2=>{},3=>{},4=>{}},
     * {5=>{},6=>{},7=>{}}
     * ]
     */

    public Map<Long, Object> getBom(Long skuId, int number, int selfNum) {

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
//            Map<Long, Object> map = new HashMap<>();
            List<Map<Long, Object>> list = new ArrayList<>();
            for (ErpPartsDetail erpPartsDetail : details) {
                list.add(this.getBom(erpPartsDetail.getSkuId(), erpPartsDetail.getNumber() * number, erpPartsDetail.getNumber()));
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
                skuNumber.setNum(o.getNum() * selfNum);
                tmp.put(aLong, skuNumber);
            }
//            for (Map<Long, Object> objectMap : list) {
//                for (Long aLong : objectMap.keySet()) {
//                    SkuNumber sn = (SkuNumber) objectMap.get(aLong);
//                    if (ToolUtil.isNotEmpty(tmp.get(aLong))) {
//                        SkuNumber sk = (SkuNumber) tmp.get(aLong);
//                        sk.setNum(sk.getNum() + sn.getNum()*number);
//                        tmp.put(aLong, sk);
//                    } else {
//                        tmp.put(aLong, sn);
//                    }
//                }
//            }

        } else {
            Integer num = 0;
            if (ToolUtil.isNotEmpty(skuList.get(skuId))) {
                num = skuList.get(skuId);
            }
            skuList.put(skuId, number + num);
            SkuNumber skuNumber = new SkuNumber();
            skuNumber.setNum(selfNum);
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
                    
                    SkuNumber skuNumber = (SkuNumber) child.get(skuId);
                    long l = number * skuNumber.getNum();
                    Long lastNum = stockNumber.get(skuId);
                    if (ToolUtil.isNotEmpty(lastNum)) {
                        stockNumber.put(skuId, lastNum + l);
                    }
                }
            } else {
                stockNumber.put(bomSkuId, 0L);
            }
        }


    }


    public void getMix(Long skuId, int number) {

        Map<String, Map<Long, Object>> bomMap = Bom.get(skuId);
        List<Long> noEnough = new ArrayList<>();

        Map<Long, Object> lastChild = bomMap.get("lastChild");
        for (Long id : lastChild.keySet()) {

            SkuNumber skuNumber = (SkuNumber) lastChild.get(id);
            int count = skuNumber.getNum() * number;   //总够需要的
            Long stockNum = stockNumber.get(id);
            if (count > stockNum) {                   //缺料
                long noNumber = count - stockNum;
                long mix = skuNumber.getNum() / noNumber;
                noEnough.add(mix);
                stockNumber.put(id, noNumber);
            }
            Long min = Collections.min(noEnough);
            mix.put(skuId, min);
        }

    }
}



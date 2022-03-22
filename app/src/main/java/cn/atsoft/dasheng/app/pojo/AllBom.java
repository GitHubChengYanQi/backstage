package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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

    private Map<Long, Map<Long, Long>> notEnough = new HashMap<>();  //缺料数

    private Map<Long, Map<String, Map<Long, Object>>> Bom = new HashMap<>();

    private Map<Long, Integer> skuList = new HashMap<>();  //生产一套所需要的物料和数量

    private Map<Long, Long> stockNumber = new HashMap<>();  //库存数量

    private Map<Long, Long> mix = new HashMap<>();   //最少可生产数量;

    private Map<Long, List<Object>> owe = new HashMap<>();  //缺料信息


    public void start(List<AllBomParam.skuNumberParam> params) {

        /**
         * 获取bom
         */
        for (AllBomParam.skuNumberParam param : params) {
            getBom(param.getSkuId(), 1, 0);
        }

        /**
         * 计算库存
         */
        getNumber();

        /**
         *  开始计算
         */
        for (AllBomParam.skuNumberParam param : params) {
            getMix(param.getSkuId(), param.getNumber());
        }

        /**
         * 返回结构
         */
        for (AllBomParam.skuNumberParam param : params) {
            Map<Long, Long> notMap = notEnough.get(param.getSkuId());

            List<Object> objects = new ArrayList<>();

            if (ToolUtil.isNotEmpty(notMap)) {
                List<Long> skuIds = new ArrayList<>(notMap.keySet());
                List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
                for (Long skuId : notMap.keySet()) {
                    for (SkuResult skuResult : skuResults) {
                        if (skuId.equals(skuResult.getSkuId())) {
                            objects.add(skuResult);
                            break;
                        }
                    }
                }

            }
            owe.put(param.getSkuId(), objects);
        }

    }

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
    public void getMix(Long skuId, Long number) {

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
                Long num = stockNumber.get(id);
                SkuNumber skuNumber = (SkuNumber) lastChild.get(id);
                notEnough.put(skuId, new HashMap<Long, Long>() {{
                    put(id, skuNumber.getNum() - num);
                }});
            }
        }
    }

}



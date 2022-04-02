package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.QualityCheckService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ProductionWorkOrderMapper;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单 服务实现类
 * </p>
 *
 * @author
 * @since 2022-02-28
 */
@Service
public class ProductionWorkOrderServiceImpl extends ServiceImpl<ProductionWorkOrderMapper, ProductionWorkOrder> implements ProductionWorkOrderService {

    @Autowired
    private PartsService partsService;

    @Autowired
    private ActivitiProcessService processService;


    @Autowired
    private StepsService stepsService;

    @Autowired
    private ActivitiSetpSetService activitiSetpSetService;

    @Autowired
    private ActivitiSetpSetDetailService activitiSetpSetDetailService;

    @Autowired
    private ProductionWorkOrderService workOrderService;

    @Autowired
    private StepProcessService stepProcessService;

    @Autowired
    private ShipSetpService shipSetpService;

    @Autowired
    private ProductionStationService productionStationService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private QualityCheckService qualityCheckService;


    @Autowired
    private GetOrigin origin;

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    private ProductionPlanService productionPlanService;
    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private StockDetailsService stockDetailsService;


    @Override
    public void add(ProductionWorkOrderParam param) {
        ProductionWorkOrder entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionWorkOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionWorkOrderParam param) {
        ProductionWorkOrder oldEntity = getOldEntity(param);
        ProductionWorkOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionWorkOrderResult findBySpec(ProductionWorkOrderParam param) {
        return null;
    }

    @Override
    public List<ProductionWorkOrderResult> findListBySpec(ProductionWorkOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionWorkOrderResult> findPageBySpec(ProductionWorkOrderParam param) {
        Page<ProductionWorkOrderResult> pageContext = getPageContext();
        IPage<ProductionWorkOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionWorkOrderParam param) {
        return param.getWorkOrderId();
    }

    private Page<ProductionWorkOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionWorkOrder getOldEntity(ProductionWorkOrderParam param) {
        return this.getById(getKey(param));
    }

    private ProductionWorkOrder getEntity(ProductionWorkOrderParam param) {
        ProductionWorkOrder entity = new ProductionWorkOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void microServiceAdd(Object param) {
        List<ProductionPlanDetail> productionPlanDetails = JSON.parseArray(param.toString(), ProductionPlanDetail.class);
//        List<Long> skuIds = new ArrayList<>();  //先取出物料   去bom中查找
        for (ProductionPlanDetail productionPlanDetail : productionPlanDetails) {

            ActivitiProcess process = processService.query().eq("form_id", productionPlanDetail.getSkuId()).eq("display", 1).eq("type", "ship").one();

            ActivitiStepsResult activitiStepsResult = stepsService.detail(process.getProcessId());


            List<ActivitiStepsResult> treeListResults = new ArrayList<>();
            getTree2List(activitiStepsResult, treeListResults);
            getTree2List2(treeListResults);
            List<ProductionWorkOrder> workOrders = new ArrayList<>();
            this.loopCreateWorkOrder(productionPlanDetail, treeListResults, productionPlanDetail.getPlanNumber(), workOrders);
            this.saveBatch(workOrders);
            for (ProductionWorkOrder workOrder : workOrders) {
                workOrder.setSource("productionPlan");
                workOrder.setSourceId(productionPlanDetail.getProductionPlanId());
                String origin = this.origin.newThemeAndOrigin("workOrder", workOrder.getWorkOrderId(), workOrder.getSource(), workOrder.getSourceId());
                workOrder.setOrigin(origin);
            }


        }


    }

    //递归添加工单
    private void loopCreateWorkOrder(ProductionPlanDetail productionPlanDetail, List<ActivitiStepsResult> stepsResultList, int num, List<ProductionWorkOrder> workOrders) {
        for (ActivitiStepsResult activitiStepsResult : stepsResultList) {

            switch (activitiStepsResult.getStepType()) {
                case "ship":

                    List<ActivitiStepsResult> list = JSON.parseArray(JSON.toJSONString(activitiStepsResult.getChildRouteSteps()), ActivitiStepsResult.class);
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getProcess()) && ToolUtil.isNotEmpty(activitiStepsResult.getProcess().getNum())) {
                        loopCreateWorkOrder(productionPlanDetail, list, activitiStepsResult.getProcess().getNum() * num, workOrders);

                    } else {
                        loopCreateWorkOrder(productionPlanDetail, list, num * 1, workOrders);
                    }
                    break;
//                case "shipStart":
                case "setp":
                    ProductionWorkOrder workOrder = new ProductionWorkOrder();
                    workOrder.setStepsId(activitiStepsResult.getSetpsId());
                    workOrder.setSourceId(productionPlanDetail.getProductionPlanId());
                    workOrder.setSource("productionPlan");
                    workOrder.setCardSkuId(productionPlanDetail.getSkuId());
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet()) && ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet().getSetpSetDetails())) {
                        for (ActivitiSetpSetDetailResult setpSetDetailResult : activitiStepsResult.getSetpSet().getSetpSetDetails()) {
                            workOrder.setSkuId(setpSetDetailResult.getSkuId());
                            workOrder.setCount(num);
                            workOrder.setShipSetpId(activitiStepsResult.getSetpSet().getShipSetpId());
                            if (setpSetDetailResult.getType().equals("in")) {
                                workOrder.setInSkuNumber(productionPlanDetail.getPlanNumber());
                                workOrder.setInSkuId(productionPlanDetail.getSkuId());
                            } else if (setpSetDetailResult.getType().equals("out")) {
                                workOrder.setOutSkuNumber(productionPlanDetail.getPlanNumber());
                                workOrder.setOutSkuId(productionPlanDetail.getSkuId());
                            }

                        }

                    }
                    workOrders.add(workOrder);
                    break;
            }

        }
    }


    private void getTree2List(ActivitiStepsResult activitiStepsResult, List<ActivitiStepsResult> results) {
        switch (activitiStepsResult.getStepType()) {
            case "shipStart":
            case "ship":
            case "setp":
                results.add(activitiStepsResult);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    getTree2List(activitiStepsResult.getChildNode(), results);
                }
                break;
            case "route":
//                results.add(activitiStepsResult);
//                results.addAll(activitiStepsResult.getConditionNodeList());
                for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                    getTree2List(stepsResult, results);
                }
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    results.add(activitiStepsResult.getChildNode());
                    getTree2List(activitiStepsResult.getChildNode(), results);
                }
                break;
        }

    }

    private void getTree2List2(List<ActivitiStepsResult> list) {
        for (ActivitiStepsResult activitiStepsResult : list) {
            if (activitiStepsResult.getStepType().equals("ship")) {
                ActivitiStepsResult detail = stepsService.detail(activitiStepsResult.getFormId());
                List<ActivitiStepsResult> treeListResults = new ArrayList<>();
                getTree2List(detail, treeListResults);
                activitiStepsResult.setChildRouteSteps(treeListResults);

                getTree2List2(treeListResults);

            }
        }
    }


    @Override
    public List<ProductionWorkOrderResult> resultsBySourceIds(String source, List<Long> sourceIds) {
        List<ProductionWorkOrder> productionWorkOrders = sourceIds.size() == 0 ? new ArrayList<>() : this.query().eq("source", source).in("source_id", sourceIds).list();
        List<ProductionWorkOrderResult> results = new ArrayList<>();
        for (ProductionWorkOrder productionWorkOrder : productionWorkOrders) {
            ProductionWorkOrderResult result = new ProductionWorkOrderResult();
            ToolUtil.copyProperties(productionWorkOrder, result);
            results.add(result);
        }
        this.format(results);

        return results;
    }

    @Override
    public List<ProductionWorkOrderResult> resultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionWorkOrder> workOrders = this.query().in("work_order_id", ids).list();
        List<ProductionWorkOrderResult> results = new ArrayList<>();
        for (ProductionWorkOrder workOrder : workOrders) {
            ProductionWorkOrderResult result = new ProductionWorkOrderResult();
            ToolUtil.copyProperties(workOrder, result);
            results.add(result);
        }
        this.format(results);
        return results;
    }


    public void format(List<ProductionWorkOrderResult> param) {
        /**
         * 查询库存数
         */
        Map<Long, Long> skuNumber = new HashMap<>();
        List<StockDetails> stockDetails = stockDetailsService.query().select("sku_id ,sum(number)as num").groupBy("sku_id").list();
        for (StockDetails stockDetail : stockDetails) {
            skuNumber.put(stockDetail.getSkuId(), stockDetail.getNum());
        }


        List<Long> stepsIds = new ArrayList<>();
        List<Long> workOrderIds = new ArrayList<>();
        List<Long> productionPlanId = new ArrayList<>();
        for (ProductionWorkOrderResult productionWorkOrderResult : param) {
            stepsIds.add(productionWorkOrderResult.getStepsId());
            if (productionWorkOrderResult.getSource().equals("productionPlan")) {
                productionPlanId.add(productionWorkOrderResult.getSourceId());
            }
            workOrderIds.add(productionWorkOrderResult.getWorkOrderId());
        }
        List<ActivitiSetpSetResult> setpSetsResult = activitiSetpSetService.getResultByStepsId(stepsIds);
        List<Long> stationIds = new ArrayList<>();
        List<Long> shipSetpIds = new ArrayList<>();
        for (ActivitiSetpSetResult setpSetResult : setpSetsResult) {
            stationIds.add(setpSetResult.getProductionStationId());
            shipSetpIds.add(setpSetResult.getShipSetpId());
        }
        List<Long> skuIds = new ArrayList<>();
        List<Long> qualityCheckIds = new ArrayList<>();
        List<ProductionStationResult> stations = productionStationService.getResultsByIds(stationIds);
        List<ShipSetpResult> shipSetps = shipSetpService.getResultsByids(shipSetpIds);
        List<ActivitiSetpSetDetailResult> shipSetDetails = activitiSetpSetDetailService.getResultByStepsIds(stepsIds);
        for (ActivitiSetpSetDetailResult shipSetDetail : shipSetDetails) {
            skuIds.add(shipSetDetail.getSkuId());
            qualityCheckIds.add(shipSetDetail.getQualityId());
            qualityCheckIds.add(shipSetDetail.getMyQualityId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        /**
         * 查询生产计划  获取卡片数量
         */

        List<ProductionPlanDetail> productionPlanDetails = productionPlanId.size() == 0 ? new ArrayList<>() : productionPlanDetailService.query().in("production_plan_id", productionPlanId).list();
        /**
         * 查询工单对应派发任务
         */
        List<ProductionTaskResult> taskResults = productionTaskService.resultsByWorkOrderIds(workOrderIds);
        /**
         * 匹配数据
         */
        for (ActivitiSetpSetResult setpSetResult : setpSetsResult) {

            for (ProductionStationResult station : stations) {
                if (setpSetResult.getProductionStationId().equals(station.getProductionStationId())) {
                    setpSetResult.setProductionStation(station);
                }
            }
            for (ShipSetpResult shipSetp : shipSetps) {
                if (setpSetResult.getShipSetpId().equals(shipSetp.getShipSetpId())) {
                    setpSetResult.setShipSetpResult(shipSetp);
                }
            }
            List<ActivitiSetpSetDetailResult> shipSetDetailResult = new ArrayList<>();
            for (ActivitiSetpSetDetailResult shipSetDetail : shipSetDetails) {
                if (setpSetResult.getSetpsId().equals(shipSetDetail.getSetpsId())) {
                    for (SkuResult skuResult : skuResults) {
                        if (skuResult.getSkuId().equals(shipSetDetail.getSkuId())) {
                            shipSetDetail.setSkuResult(skuResult);
                        }
                    }
                    shipSetDetailResult.add(shipSetDetail);
                }
            }
            setpSetResult.setSetpSetDetails(shipSetDetailResult);

        }


        for (ProductionWorkOrderResult result : param) {

            Long number = skuNumber.get(result.getSkuId());
            if (ToolUtil.isNotEmpty(number)) {
                result.setStockNumber(number);
            }

            for (ActivitiSetpSetResult setpSetResult : setpSetsResult) {
                if (result.getStepsId().equals(setpSetResult.getSetpsId())) {
                    result.setSetpSetResult(setpSetResult);
                }
            }
            int completeNum = 0;
            int toDoNum = 0;
            for (ProductionTaskResult taskResult : taskResults) {

                if (taskResult.getWorkOrderId().equals(result.getWorkOrderId()) && taskResult.getStatus().equals(99)) {
                    completeNum += taskResult.getNumber();
                }
                if (taskResult.getWorkOrderId().equals(result.getWorkOrderId()) && !taskResult.getStatus().equals(99)) {
                    toDoNum += taskResult.getNumber();
                }
            }
            result.setCompleteNum(completeNum);
            result.setToDoNum(toDoNum);
            for (ProductionPlanDetail productionPlanDetail : productionPlanDetails) {
                if (productionPlanDetail.getProductionPlanId().equals(result.getSourceId()) && result.getSource().equals("productionPlan") && result.getCardSkuId().equals(productionPlanDetail.getSkuId())) {
                    result.setCardNumber(productionPlanDetail.getPlanNumber());
                }
            }
        }

    }
}

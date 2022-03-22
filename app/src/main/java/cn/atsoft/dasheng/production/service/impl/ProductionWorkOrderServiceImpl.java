package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.mapper.ProductionWorkOrderMapper;
import cn.atsoft.dasheng.production.model.ProcessRouteActivitiStepsRequest;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.production.service.ProductionStationService;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private GetOrigin origin;


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

              ActivitiProcess process = processService.query().eq("form_id", productionPlanDetail.getSkuId()).eq("status", 99).eq("type","ship").one();

            ActivitiStepsResult activitiStepsResult = stepsService.detail(process.getProcessId());




            List<ActivitiStepsResult> treeListResults = new ArrayList<>();
            getTree2List(activitiStepsResult, treeListResults);
            getTree2List2(treeListResults);
            List<ProductionWorkOrder> workOrders = new ArrayList<>();
            this.loopCreateWorkOrder(productionPlanDetail,treeListResults, productionPlanDetail.getPlanNumber(), workOrders);
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
    private void loopCreateWorkOrder(ProductionPlanDetail productionPlanDetail,List<ActivitiStepsResult> stepsResultList, int num, List<ProductionWorkOrder> workOrders) {
        for (ActivitiStepsResult activitiStepsResult : stepsResultList) {

            switch (activitiStepsResult.getStepType()) {
//                case "ship":
//
//                    List<ActivitiStepsResult> list = JSON.parseArray(JSON.toJSONString(activitiStepsResult.getChildRouteSteps()), ActivitiStepsResult.class);
//                    if ( ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet()) && ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet().getSetpSetDetails()) && activitiStepsResult.getSetpSet().getSetpSetDetails().size() == 1 && ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet().getSetpSetDetails().get(0).getNum())){
//                        loopCreateWorkOrder(productionPlanDetail,list, activitiStepsResult.getSetpSet().getSetpSetDetails().get(0).getNum() * num, workOrders);
//
//                    }else {
//                        loopCreateWorkOrder(productionPlanDetail,list, num * 1, workOrders);
//                    }
//                    break;
//                case "shipStart":
                case "setp":
                    ProductionWorkOrder workOrder = new ProductionWorkOrder();
                    workOrder.setStepsId(activitiStepsResult.getSetpsId());
                    workOrder.setSourceId(productionPlanDetail.getProductionPlanId());
                    workOrder.setSource("productionPlan");
                    if ( ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet()) && ToolUtil.isNotEmpty(activitiStepsResult.getSetpSet().getSetpSetDetails()) ) {
                        for (ActivitiSetpSetDetailResult setpSetDetailResult : activitiStepsResult.getSetpSet().getSetpSetDetails()) {
                            workOrder.setSkuId(setpSetDetailResult.getSkuId());
                            workOrder.setCount(num);
                            workOrder.setShipSetpId(activitiStepsResult.getSetpSet().getShipSetpId());
                            if (setpSetDetailResult.getType().equals("in")) {
                                workOrder.setInSkuNumber(num * setpSetDetailResult.getNum());
                                workOrder.setInSkuId(setpSetDetailResult.getSkuId());
                            } else if (setpSetDetailResult.getType().equals("out")) {
                                workOrder.setOutSkuNumber(num * setpSetDetailResult.getNum());
                                workOrder.setOutSkuId(setpSetDetailResult.getSkuId());
                            }

                        }

                    }
                    workOrders.add(workOrder);
                    break;
            }

        }
    }


//    private void createWorkOrder(ProductionPlanDetail productionPlanDetail, List<ActivitiSetpSetResult> activitiSetpSetResults) {
//        List<ProductionWorkOrder> workOrders = new ArrayList<>();
//        for (ActivitiSetpSetResult activitiSetpSetResult : activitiSetpSetResults) {
//            ProductionWorkOrder workOrder = new ProductionWorkOrder();
//            workOrder.setShipSetpId(activitiSetpSetResult.getShipSetpId());
//            workOrder.setSource("productionPlan");
//            workOrder.setSourceId(productionPlanDetail.getProductionPlanId());
//            for (ActivitiSetpSetDetailResult setpSetDetail : activitiSetpSetResult.getSetpSetDetails()) {
//                if (ToolUtil.isNotEmpty(setpSetDetail.getType()) && setpSetDetail.getType().equals("1")) {
//                    workOrder.setInSkuId(setpSetDetail.getSkuId());
//                    workOrder.setInSkuNumber(setpSetDetail.getNum() * productionPlanDetail.getPlanNumber());
//                } else {
//                    workOrder.setOutSkuId(setpSetDetail.getSkuId());
//                    workOrder.setOutSkuNumber(setpSetDetail.getNum() * productionPlanDetail.getPlanNumber());
//                }
//
//            }
//            workOrders.add(workOrder);
//        }
//        this.saveBatch(workOrders);
//    }

    private void getTree2List(ActivitiStepsResult activitiStepsResult, List<ActivitiStepsResult> results) {
        switch (activitiStepsResult.getStepType()) {
            case "shipStart":
            case "ship":
            case "setp":
                results.add(activitiStepsResult);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())){
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
    public List<ProductionWorkOrderResult> resultsBySourceIds(String source,List<Long> sourceIds){
        List<ProductionWorkOrder> productionWorkOrders = sourceIds.size() == 0 ? new ArrayList<>() : this.query().eq("source", source).in("source_id", sourceIds).list();
        List<ProductionWorkOrderResult> results = new ArrayList<>();
        List<Long> stepsIds  = new ArrayList<>();
        for (ProductionWorkOrder productionWorkOrder : productionWorkOrders) {
            ProductionWorkOrderResult result = new ProductionWorkOrderResult();
            ToolUtil.copyProperties(productionWorkOrder,result);
            results.add(result);
            stepsIds.add(productionWorkOrder.getStepsId());
        }
        List<ActivitiSetpSetResult> setpSetsResult = activitiSetpSetService.getResultByStepsId(stepsIds);
        List<Long> stationIds = new ArrayList<>();
        List<Long> shipSetpIds = new ArrayList<>();
        for (ActivitiSetpSetResult setpSetResult : setpSetsResult) {
            stationIds.add(setpSetResult.getProductionStationId());
            shipSetpIds.add(setpSetResult.getShipSetpId());
        }


//        activitiSetpSetDetailService;

        List<ProductionStationResult> stations = productionStationService.getResultsByIds(stationIds);
        List<ShipSetpResult> shipSetps = shipSetpService.getResultsByids(shipSetpIds);
        for (ActivitiSetpSetResult setpSetResult : setpSetsResult) {
            for (ProductionStationResult station : stations) {
                if (setpSetResult.getProductionStationId().equals(station.getProductionStationId())){
                    setpSetResult.setProductionStation(station);
                }
            }
            for (ShipSetpResult shipSetp : shipSetps) {
                if (setpSetResult.getShipSetpId().equals(shipSetp.getShipSetpId())){
                    setpSetResult.setShipSetpResult(shipSetp);
                }
            }
        }

        return results;
    }
}

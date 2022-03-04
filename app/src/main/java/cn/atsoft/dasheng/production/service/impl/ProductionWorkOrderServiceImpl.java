package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.form.service.StepProcessService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.mapper.ProductionWorkOrderMapper;
import cn.atsoft.dasheng.production.model.ProcessRouteActivitiStepsRequest;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private ProcessRouteService processRouteService;


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
        List<Long> skuIds = new ArrayList<>();  //先取出物料   去bom中查找
        for (ProductionPlanDetail productionPlanDetail : productionPlanDetails) {
//            Parts designParts = partsService.query().eq("sku_id", productionPlanDetail.getSkuId()).eq("type", 1).eq("display", 1).eq("status", 99).one();
//            if (ToolUtil.isEmpty(designParts)) {
//                throw new ServiceException(500, "请先创建设计bom");
//            }
//            Parts productionParts = partsService.query().eq("sku_id", productionPlanDetail.getSkuId()).eq("type", 2).eq("display", 1).eq("status", 99).one();
//            if (ToolUtil.isEmpty(productionParts)) {
//                throw new ServiceException(500, "请先创建生产bom");
//            }
            ProcessRoute processRouteByparts = processRouteService.query().eq("sku_id", productionPlanDetail.getSkuId()).eq("status", 99).one();
//            if (ToolUtil.isEmpty(processRouteByparts)) {
//                throw new ServiceException(500, "请先创建" + productionParts.getPartName() + "的工艺路线");
//            }
            ActivitiStepsResult activitiStepsResult = stepsService.detail(processRouteByparts.getProcessRouteId());


            List<ActivitiSetpSetDetail> setDetailSByRouId = stepProcessService.getSetDetailSByRouId(processRouteByparts.getProcessRouteId());


            List<ActivitiStepsResult> treeListResults = new ArrayList<>();
            getTree2List(activitiStepsResult,treeListResults);


            List<ActivitiStepsResult> stepsResults = stepsService.getStepsResultByFormId(processRouteByparts.getProcessRouteId());
            List<ActivitiStepsResult> stepsResultList = formatSetpSetAndDetail(stepsResults);
            loopChildrenShip(stepsResultList);


            List<ProductionWorkOrder> workOrders = new ArrayList<>();
            loopCreateWorkOrder(stepsResultList, productionPlanDetail.getPlanNumber(), workOrders);
//            workOrderService.saveBatch(workOrders);


            System.out.println("====================NEW ======================");
            System.out.println("==============================================");
            System.out.println(JSON.toJSONString(setDetailSByRouId));
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("====================DETAILJSON======================");
            System.out.println("==============================================");
            System.out.println(JSON.toJSONString(activitiStepsResult));
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("====================JSON======================");
            System.out.println("==============================================");
            System.out.println(JSON.toJSONString(stepsResultList));
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("====================WorkOrder======================");
            System.out.println("==============================================");
            System.out.println(JSON.toJSONString(workOrders));
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("==============================================");

        }


    }

    private void childrenShip(ActivitiStepsResult childrenShip) {
        List<ActivitiStepsResult> stepsResults = stepsService.getStepsResultByFormId(childrenShip.getFormId());
        List<ActivitiStepsResult> stepsResultList = formatSetpSetAndDetail(stepsResults);
        childrenShip.setProcessRoute(stepsResultList);
    }

    private void loopChildrenShip(List<ActivitiStepsResult> stepsResultList) {
        for (ActivitiStepsResult stepsResult : stepsResultList) {
            if (stepsResult.getStepType().equals("ship")) {
                childrenShip(stepsResult);
                List<ActivitiStepsResult> list = JSON.parseArray(JSON.toJSONString(stepsResult.getProcessRoute()), ActivitiStepsResult.class);
                loopChildrenShip(list);
            }
        }
    }

    private List<ActivitiStepsResult> formatSetpSetAndDetail(List<ActivitiStepsResult> stepsResults) {
        List<Long> stepsIds = new ArrayList<>();
        for (ActivitiStepsResult stepsResult : stepsResults) {
            stepsIds.add(stepsResult.getSetpsId());
        }
        List<ActivitiSetpSet> activitiSetpSets = stepsIds.size() == 0 ? new ArrayList<>() : activitiSetpSetService.query().in("setps_id", stepsIds).eq("display", 1).list();

        List<ActivitiSetpSetDetail> activitiSetpSetDetails = stepsIds.size() == 0 ? new ArrayList<>() : activitiSetpSetDetailService.query().in("setps_id", stepsIds).eq("display", 1).list();
        for (ActivitiStepsResult stepsResult : stepsResults) {
            /**
             * 工序步骤
             */
            for (ActivitiSetpSet activitiSetpSet : activitiSetpSets) {
                if (stepsResult.getSetpsId().equals(activitiSetpSet.getShipSetpId())) {
                    ActivitiSetpSetResult setpSetResult = new ActivitiSetpSetResult();
                    ToolUtil.copyProperties(activitiSetpSet, setpSetResult);
                    stepsResult.setSetpSetResult(setpSetResult);
                }
            }
            /**
             * 匹配详情
             */
            List<ActivitiSetpSetDetailResult> setpSetDetailResults = new ArrayList<>();
            for (ActivitiSetpSetDetail activitiSetpSetDetail : activitiSetpSetDetails) {
                ActivitiSetpSetDetailResult activitiSetpSetDetailResult = new ActivitiSetpSetDetailResult();
                ToolUtil.copyProperties(activitiSetpSetDetail, activitiSetpSetDetailResult);
                setpSetDetailResults.add(activitiSetpSetDetailResult);
            }
            stepsResult.setSetpSetDetailResults(setpSetDetailResults);
        }

        List<ActivitiStepsResult> allSteps = new ArrayList<>();
        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getSupper() == 0 && ToolUtil.isNotEmpty(stepsResult.getChildren())) {
                getStepList(stepsResults, stepsResult, allSteps);
            }
        }
        return allSteps;

    }

    private void getStepList(List<ActivitiStepsResult> list, ActivitiStepsResult children, List<ActivitiStepsResult> all) {
        if (ToolUtil.isNotEmpty(children) && ToolUtil.isNotEmpty(children.getStepType())) {
            switch (children.getStepType()) {
                case "route":
                    List<ActivitiStepsResult> conditionList = new ArrayList<>();
                    List<Long> childrensIds = Arrays.asList(children.getConditionNodes().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    for (Long childrensId : childrensIds) {
                        for (ActivitiStepsResult activitiStepsResult : list) {
                            if (childrensId.equals(activitiStepsResult.getSetpsId())) {
                                conditionList.add(activitiStepsResult);
                            }
                        }
                    }
                    children.setConditionNodeList(conditionList);
                    all.addAll(children.getConditionNodeList());
                    for (ActivitiStepsResult activitiStepsResult : children.getConditionNodeList()) {
                        getStepList(list, activitiStepsResult, all);
                    }
                    if (ToolUtil.isNotEmpty(children.getChildNode())) {
                        all.add(children.getChildNode());
                        getStepList(list, children.getChildNode(), all);
                    }
                    break;

                default:
                    for (ActivitiStepsResult activitiStepsResult : list) {
                        if (children.getSetpsId().equals(activitiStepsResult.getSetpsId())) {
                            all.add(children);
                        }
                    }
                    for (ActivitiStepsResult activitiStepsResult : list) {

                        if (ToolUtil.isNotEmpty(children.getChildren()) && Long.valueOf(children.getChildren()).equals(activitiStepsResult.getSetpsId())) {
                            getStepList(list, activitiStepsResult, all);
                        }
                    }

                    break;
            }
        }
    }


    private void loopCreateWorkOrder(List<ActivitiStepsResult> stepsResultList, int num, List<ProductionWorkOrder> workOrders) {
        for (ActivitiStepsResult activitiStepsResult : stepsResultList) {
            ProductionWorkOrder workOrder = new ProductionWorkOrder();

            switch (activitiStepsResult.getStepType()) {
                case "ship":
                    List<ActivitiStepsResult> list = JSON.parseArray(JSON.toJSONString(activitiStepsResult.getProcessRoute()), ActivitiStepsResult.class);
//                    loopCreateWorkOrder(list,num*activitiStepsResult.getSetpSetDetailResults().get(0).getNum(),workOrders);//如果是正常情况下大概这么取
                    loopCreateWorkOrder(list, num * 1, workOrders);
                    break;
                case "shipStart":
                case "setp":
                    for (ActivitiSetpSetDetailResult setpSetDetailResult : activitiStepsResult.getSetpSetDetailResults()) {

                        workOrder.setSkuId(setpSetDetailResult.getSkuId());
                        workOrder.setCount(num);
                        if (setpSetDetailResult.getType().equals("in")) {
                            workOrder.setInSkuNumber(num * setpSetDetailResult.getNum());
                            workOrder.setInSkuId(setpSetDetailResult.getSkuId());
                        } else if (setpSetDetailResult.getType().equals("out")) {
                            workOrder.setOutSkuNumber(num * setpSetDetailResult.getNum());
                            workOrder.setOutSkuId(setpSetDetailResult.getSkuId());
                        }
                        workOrders.add(workOrder);
                    }
            }
        }
    }


    private void createWorkOrder(ProductionPlanDetail productionPlanDetail, List<ActivitiSetpSetResult> activitiSetpSetResults) {
        List<ProductionWorkOrder> workOrders = new ArrayList<>();
        for (ActivitiSetpSetResult activitiSetpSetResult : activitiSetpSetResults) {
            ProductionWorkOrder workOrder = new ProductionWorkOrder();
            workOrder.setShipSetpId(activitiSetpSetResult.getShipSetpId());
            workOrder.setSource("productionPlan");
            workOrder.setSourceId(productionPlanDetail.getProductionPlanId());
            for (ActivitiSetpSetDetailResult setpSetDetail : activitiSetpSetResult.getSetpSetDetails()) {
                if (ToolUtil.isNotEmpty(setpSetDetail.getType()) && setpSetDetail.getType().equals("1")) {
                    workOrder.setInSkuId(setpSetDetail.getSkuId());
                    workOrder.setInSkuNumber(setpSetDetail.getNum() * productionPlanDetail.getPlanNumber());
                } else {
                    workOrder.setOutSkuId(setpSetDetail.getSkuId());
                    workOrder.setOutSkuNumber(setpSetDetail.getNum() * productionPlanDetail.getPlanNumber());
                }

            }
            workOrders.add(workOrder);
        }
        this.saveBatch(workOrders);
    }

    private void getTree2List(ActivitiStepsResult activitiStepsResult, List<ActivitiStepsResult> results) {
        switch (activitiStepsResult.getStepType()) {
            case "shipStart":
                results.add(activitiStepsResult);
                getTree2List(activitiStepsResult.getChildNode(),results);
                break;
            case "ship":
            case "setp":
                results.add(activitiStepsResult);
                break;
            case "route":
                results.add(activitiStepsResult);
//                results.addAll(activitiStepsResult.getConditionNodeList());
                for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                    getTree2List(stepsResult,results);
                }
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    results.add(activitiStepsResult.getChildNode());
                    getTree2List(activitiStepsResult.getChildNode(),results);
                }
                break;
        }

    }

    private void getTree2List2(ActivitiStepsResult activitiStepsResult, List<ActivitiStepsResult> list) {
//        for (list)
    }


}

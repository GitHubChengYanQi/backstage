package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.log.LogInputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  生产计划主表 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Service
public class ProductionPlanServiceImpl extends ServiceImpl<ProductionPlanMapper, ProductionPlan> implements ProductionPlanService {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ProcessRouteServiceImpl processRouteService;

    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    @Autowired
    private PartsService partsService;

    @Autowired
    private StepsService stepsService;

    @Override
    public void add(ProductionPlanParam param){
        ProductionPlan entity = getEntity(param);
        this.save(entity);
        List<Long> skuIds = new ArrayList<>();

        List<ProductionPlanDetail> details = new ArrayList<>();
        for (ProductionPlanDetailParam productionPlanDetailParam : param.getProductionPlanDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            ToolUtil.copyProperties(productionPlanDetailParam,detail);
            detail.setProductionPlanId(entity.getProductionPlanId());
            skuIds.add(detail.getSkuId());
            details.add(detail);
        }
//        Integer designParts = partsService.query().in("sku_id", skuIds).eq("type", 1).eq("display", 1).eq("status", 99).count();
//        if (designParts<skuIds.size()) {
//            int i = skuIds.size() - designParts;
//            throw new ServiceException(500, "有"+i+"个物品没有设计bom,请先创建设计bom");
//        }
//        Integer productionParts = partsService.query().in("sku_id", skuIds).eq("type", 2).eq("display", 1).eq("status", 99).count();
//        if (productionParts<skuIds.size()) {
//            int i = skuIds.size() - productionParts;
//            throw new ServiceException(500, "有"+i+"个物品你没有生产bom,请先创建生产bom");
//        }
        productionPlanDetailService.saveBatch(details);
        if (details.size() > 0) {    //调用消息队列
            MicroServiceEntity serviceEntity = new MicroServiceEntity();
            serviceEntity.setType(MicroServiceType.WORK_ORDER);
            serviceEntity.setOperationType(OperationType.ADD);
            String jsonString = JSON.toJSONString(details);
            serviceEntity.setObject(jsonString);
            serviceEntity.setMaxTimes(2);
            serviceEntity.setTimes(0);
            messageProducer.microService(serviceEntity);
        }

    }

    @Override
    public void delete(ProductionPlanParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanParam param){
        ProductionPlan oldEntity = getOldEntity(param);
        ProductionPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanResult findBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public List<ProductionPlanResult> findListBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPlanResult> findPageBySpec(ProductionPlanParam param){
        Page<ProductionPlanResult> pageContext = getPageContext();
        IPage<ProductionPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<ProductionPlanResult> param){
        List<Long> planIds = new ArrayList<>();

        for (ProductionPlanResult productionPlanResult : param) {
            planIds.add(productionPlanResult.getProductionPlanId());
        }
        /**
         * 查询对应的工单集合
         */
        List<ProductionWorkOrderResult> workOrderResults = productionWorkOrderService.resultsBySourceIds("productionPlan", planIds);

        /**
         * 查询出子表数据
         */
        List<ProductionPlanDetailResult> detailResults = planIds.size() == 0 ? new ArrayList<>() : productionPlanDetailService.resultsByPlanIds(planIds);
        /**
         * 子表数据取出sku_id 去查找工艺路线  工艺路线去查找步骤树形   然后根据树形去匹配任务
         */
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPlanDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
        List<ProcessRouteResult> routeResults = skuIds.size() == 0 ? new ArrayList<>() : processRouteService.resultsBySkuIds(skuIds);
        //将步骤set进工艺路线中方便匹配
        ActivitiStepsResult detail = new ActivitiStepsResult();
        for (ProcessRouteResult routeResult : routeResults) {
            detail = stepsService.detail(routeResult.getProcessRouteId());
            routeResult.setStepsResult(detail);
        }
        for (ProductionPlanResult productionPlanResult : param) {
            List<ProductionPlanDetailResult> results = new ArrayList<>();
            for (ProductionPlanDetailResult detailResult : detailResults) {
                if (productionPlanResult.getProductionPlanId().equals(detailResult.getProductionPlanId())){
                    formatTreeProcessRoute(detail,workOrderResults);
                    results.add(detailResult);
                }
            }
            for (ProductionPlanDetailResult result : results) {
                for (ProcessRouteResult routeResult : routeResults) {
                    if (result.getSkuId().equals(routeResult.getSkuId())){
                        result.setProcessRouteResult(routeResult);
                    }
                }
            }
            productionPlanResult.setPlanDetailResults(results);
        }

    }




    private void formatTreeProcessRoute(ActivitiStepsResult detail, List<ProductionWorkOrderResult> workOrderResults){
       if(ToolUtil.isNotEmpty(detail)){
           switch (detail.getStepType()) {
               case "ship":
                   for (ProductionWorkOrderResult workOrderResult : workOrderResults) {
                       if (detail.getSetpsId().equals(workOrderResult.getStepsId())){
                           detail.setWorkOrderResult(workOrderResult);
                           break;
                       }
                   }
                   ProcessRouteResult processRoute = (ProcessRouteResult) detail.getProcessRoute();
                   formatTreeProcessRoute(processRoute.getStepsResult(),workOrderResults);
                   break;
               case "shipStart":
               case "setp":
                   for (ProductionWorkOrderResult workOrderResult : workOrderResults) {
                       if (detail.getSetpsId().equals(workOrderResult.getStepsId())){
                           detail.setWorkOrderResult(workOrderResult);
                           break;
                       }
                   }
                   if(ToolUtil.isNotEmpty(detail.getChildNode())){
                       formatTreeProcessRoute(detail.getChildNode(),workOrderResults);
                   }

                   break;
               case "route":
                   for (ActivitiStepsResult activitiStepsResult : detail.getConditionNodeList()) {
                       formatTreeProcessRoute(activitiStepsResult,workOrderResults);
                   }
                   break;
           }
       }
    }

    private Serializable getKey(ProductionPlanParam param){
        return param.getProductionPlanId();
    }

    private Page<ProductionPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlan getOldEntity(ProductionPlanParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlan getEntity(ProductionPlanParam param) {
        ProductionPlan entity = new ProductionPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.*;
import cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 生产计划主表 服务实现类
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
    private PartsService partsService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ProcessRouteServiceImpl processRouteService;

    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StepsService stepsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductionCardService productionCardService;

    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private CodingRulesService codingRulesService;

    @Override
    public void add(ProductionPlanParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "13").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置生产计划单据编码规则");
            }
        }
        List<Long> skuIds = new ArrayList<>();
        List<ProductionPlanDetail> details = new ArrayList<>();
        for (OrderDetailParam orderDetailParam : param.getOrderDetailParams()) {

            skuIds.add(orderDetailParam.getSkuId());

        }
        skuIds=skuIds.stream().distinct().collect(Collectors.toList());
        Integer designParts = partsService.query().in("sku_id", skuIds).eq("display", 1).eq("status",99).count();
        if (designParts<skuIds.size()) {
            int i = skuIds.size() - designParts;
            throw new ServiceException(500, "有"+i+"个物品没有设计bom,请先创建设计bom");
        }
        List<ActivitiProcess> prosess = activitiProcessService.query().in("form_id", skuIds).eq("type", "ship").eq("display", 1).list();

        if (skuIds.size()!=prosess.size()){
            throw new ServiceException(500,"有产品没有工艺路线,请先创建工艺路线");
        }
        ProductionPlan entity = getEntity(param);
        this.save(entity);
        for (OrderDetailParam orderDetailParam : param.getOrderDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            detail.setProductionPlanId(entity.getProductionPlanId());
            detail.setOrderDetailId(orderDetailParam.getDetailId());
            detail.setPlanNumber(Math.toIntExact(orderDetailParam.getPurchaseNumber()));
            detail.setDeliveryDate(orderDetailParam.getDeliveryDate());
            detail.setSkuId(orderDetailParam.getSkuId());
            detail.setContractCoding(orderDetailParam.getContractCoding());
            details.add(detail);
        }
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
    public void delete(ProductionPlanParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanParam param) {
        ProductionPlan oldEntity = getOldEntity(param);
        ProductionPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanResult findBySpec(ProductionPlanParam param) {
        return null;
    }

    @Override
    public List<ProductionPlanResult> findListBySpec(ProductionPlanParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionPlanResult> findPageBySpec(ProductionPlanParam param) {
        Page<ProductionPlanResult> pageContext = getPageContext();
        IPage<ProductionPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        for (ProductionPlanResult record : page.getRecords()) {
            record.setWorkOrderResults(null);
            record.setPlanDetailResults(null);
        }
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<ProductionPlanResult> param) {
        List<Long> planIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();


        for (ProductionPlanResult productionPlanResult : param) {
            planIds.add(productionPlanResult.getProductionPlanId());
            userIds.add(productionPlanResult.getUserId());
        }


        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);

        /**
         * 查询对应的工单集合
         */
        List<ProductionWorkOrderResult> workOrderResults = planIds.size() == 0 ? new ArrayList<>() : productionWorkOrderService.resultsBySourceIds("productionPlan", planIds);

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
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        List<ProcessRouteResult> routeResults = skuIds.size() == 0 ? new ArrayList<>() : processRouteService.resultsBySkuIds(skuIds);

        //将步骤set进工艺路线中方便匹配
        ActivitiStepsResult detail = new ActivitiStepsResult();
        for (ProcessRouteResult routeResult : routeResults) {
            detail = stepsService.detail(routeResult.getProcessRouteId());
            routeResult.setStepsResult(detail);
        }
        for (ProductionPlanResult productionPlanResult : param) {
            List<ProductionWorkOrderResult> workOrderResultList = new ArrayList<>();
            for (ProductionWorkOrderResult workOrderResult : workOrderResults) {
//                for (ProductionCardResult cardResult : cardResults) {
//                    if (cardResult.getSkuId().equals())
//                }
                if (workOrderResult.getSource().equals("productionPlan") && workOrderResult.getSourceId().equals(productionPlanResult.getProductionPlanId())){
                    workOrderResultList.add(workOrderResult);
                }
            }
            productionPlanResult.setWorkOrderResults(workOrderResultList);
            List<ProductionPlanDetailResult> results = new ArrayList<>();
            for (UserResult userResultsById : userResultsByIds) {
                if (userResultsById.getUserId().equals(productionPlanResult.getUserId())){
                    productionPlanResult.setUserResult(userResultsById);
                }
            }
            for (ProductionPlanDetailResult detailResult : detailResults) {
                if (productionPlanResult.getProductionPlanId().equals(detailResult.getProductionPlanId())) {

                    results.add(detailResult);
                }
            }
            for (ProductionPlanDetailResult result : results) {
                for (ProcessRouteResult routeResult : routeResults) {
                    if (result.getSkuId().equals(routeResult.getSkuId())) {
                        result.setProcessRouteResult(routeResult);
                    }
                }
                for (SkuResult skuResult : skuResults) {
                    if (skuResult.getSkuId().equals(result.getSkuId())) {
                        result.setSkuResult(skuResult);
                    }
                }
            }
            productionPlanResult.setPlanDetailResults(results);
        }

    }



    private Serializable getKey(ProductionPlanParam param) {
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
    @Override
    public List<ProductionPlanResult> resultsByIds(List<Long> ids){
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionPlan> productionPlanList = this.listByIds(ids);
        List<ProductionPlanResult> productionPlanResults = BeanUtil.copyToList(productionPlanList, ProductionPlanResult.class, new CopyOptions());
        this.format(productionPlanResults);
        return productionPlanResults;
    }

}

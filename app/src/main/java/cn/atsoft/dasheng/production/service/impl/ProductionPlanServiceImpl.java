package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.bom.service.RestBomService;
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
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ProductionPlanMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.*;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
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

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ProductionPickListsService pickListsService;
    @Autowired
    private ProductionPickListsDetailService pickListsDetailService;

    @Autowired
    private RestBomService partsService;

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
    @Autowired
    private ProductionTaskService productionTaskService;

    @Override
    public void add(ProductionPlanParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "13").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                param.setCoding(codingRulesService.genSerial());
            }
        }
        List<Long> skuIds = new ArrayList<>();
        List<ProductionPlanDetail> details = new ArrayList<>();
        for (OrderDetailParam orderDetailParam : param.getOrderDetailParams()) {
            skuIds.add(orderDetailParam.getSkuId());
        }
        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
        Integer designParts = partsService.query().in("sku_id", skuIds).eq("display", 1).eq("status", 99).count();
        if (designParts < skuIds.size()) {
            int i = skuIds.size() - designParts;
            throw new ServiceException(500, "有" + i + "个物品没有设计bom,请先创建设计bom");
        }
        List<ActivitiProcess> prosess = activitiProcessService.query().in("form_id", skuIds).eq("type", "ship").eq("display", 1).list();

        if (skuIds.size() != prosess.size()) {
            throw new ServiceException(500, "有产品没有工艺路线,请先创建工艺路线");
        }
        ProductionPlan entity = getEntity(param);
        this.save(entity);
        for (OrderDetailParam orderDetailParam : param.getOrderDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            detail.setProductionPlanId(entity.getProductionPlanId());
            if (ToolUtil.isNotEmpty(orderDetailParam.getDetailId())) {
                detail.setOrderDetailId(orderDetailParam.getDetailId());

            }
            detail.setPlanNumber(Math.toIntExact(orderDetailParam.getPurchaseNumber()));
            detail.setDeliveryDate(orderDetailParam.getDeliveryDate());
            detail.setSkuId(orderDetailParam.getSkuId());
            detail.setContractCoding(orderDetailParam.getContractCoding());
            detail.setCustomerName(orderDetailParam.getCustomerName());
            detail.setPartsId(orderDetailParam.getPartsId());
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
    public void addByBom(ProductionPlanParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "13").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                param.setCoding(codingRulesService.genSerial());
            }
        }
        List<ProductionPlanDetail> details = new ArrayList<>();

        Long partsId = param.getProductionPlanDetailParams().get(0).getPartsId();
        int number = param.getProductionPlanDetailParams().get(0).getPlanNumber();
        RestBom parts = partsService.getById(partsId);

        ProductionPlan entity = getEntity(param);
        this.save(entity);
        int bomCount = 1;
        List<RestBomResult> byBomId = new ArrayList<>();
        for (ProductionPlanDetailParam detailParam : param.getProductionPlanDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            detail.setProductionPlanId(entity.getProductionPlanId());
            detail.setPlanNumber(detailParam.getPlanNumber());
            detail.setDeliveryDate(detailParam.getDeliveryDate());
            detail.setSkuId(parts.getSkuId());
            detail.setPartsId(detailParam.getPartsId());
            int skuCount = 0;
            byBomId = partsService.getByBomId(partsId, detail.getPlanNumber());
            byBomId.removeIf(i->(ToolUtil.isEmpty(i.getDetailResults()) || i.getDetailResults().size()==0));
            bomCount = byBomId.size()*detail.getPlanNumber();
            for (RestBomResult restBomResult : byBomId) {
                for (RestBomDetailResult detailResult : restBomResult.getDetailResults() ) {
                    if (detailResult.getAutoOutstock().equals(1)) {
                        skuCount+=detailResult.getNumber();
                    }
                }

            }
            detail.setSkuCount(skuCount);
            details.add(detail);

        }
        productionPlanDetailService.saveBatch(details);
        List<ProductionCard> cartList = new ArrayList<>();
        for (ProductionPlanDetail detail : details) {

            for (int i = 0; i < detail.getPlanNumber(); i++) {
                int finalBomCount = bomCount;
                cartList.add(new ProductionCard() {{
                    setSkuId(detail.getSkuId());
                    setSource("productionPlan");
                    setSourceId(detail.getProductionPlanId());
                    setBomCount(finalBomCount /detail.getPlanNumber());
                }});
            }
        }
        entity.setBomCount(bomCount);
        this.updateById(entity);
        productionCardService.addBatch(cartList);
        for (ProductionCard productionCard : cartList) {
            productionTaskService.createTaskByBom(partsId,number,"productionPlan",entity.getProductionPlanId(),productionCard.getProductionCardId());
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
//        for (ProductionPlanResult record : page.getRecords()) {
//            record.setWorkOrderResults(null);
//            record.setPlanDetailResults(null);
//        }
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<ProductionPlanResult> param) {
        List<Long> planIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        List<ProductionCard> cardList = planIds.size() == 0 ? new ArrayList<>() : productionCardService.lambdaQuery().eq(ProductionCard::getSource, "productionPlan").in(ProductionCard::getSourceId, planIds).list();
        userIds.addAll(param.stream().map(ProductionPlanResult::getCreateUser).distinct().collect(Collectors.toList()));
        for (ProductionPlanResult productionPlanResult : param) {
            planIds.add(productionPlanResult.getProductionPlanId());
            if (ToolUtil.isNotEmpty(productionPlanResult.getUserId())) {
                userIds.addAll(Arrays.stream(productionPlanResult.getUserId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        }

        List<ProductionPickListsResult> productionPlanList = new ArrayList<>();
        if (planIds.size()>0){

            productionPlanList = pickListsService.countNumber(new ProductionPickListsParam() {{
                setSource("productionPlan");
                setSourceIds(planIds);
            }});
        }
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds.stream().distinct().collect(Collectors.toList()));
        List<ProductionTask> productionTasks = productionTaskService.lambdaQuery().eq(ProductionTask::getSource, "productionPlan").in(ProductionTask::getSourceId, planIds).eq(ProductionTask::getStatus, 99).list();
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
            int cardCount = 0;
            int doneCartCount = 0;
            for (ProductionCard productionCard : cardList) {
                if (productionCard.getSourceId().equals(productionPlanResult.getProductionPlanId())){
                    cardCount++;
                    if (ToolUtil.isNotEmpty(productionCard.getStatus()) && productionCard.getStatus().equals(99)){
                        doneCartCount++;
                    }
                }
            }
            productionPlanResult.setCartCount(cardCount);
            productionPlanResult.setDoneCartCount(doneCartCount);
            List<ProductionWorkOrderResult> workOrderResultList = new ArrayList<>();
            for (ProductionWorkOrderResult workOrderResult : workOrderResults) {
//                for (ProductionCardResult cardResult : cardResults) {
//                    if (cardResult.getSkuId().equals())
//                }
                if (workOrderResult.getSource().equals("productionPlan") && workOrderResult.getSourceId().equals(productionPlanResult.getProductionPlanId())) {
                    workOrderResultList.add(workOrderResult);
                }
            }
            int doneBomCount = 0;
            for (ProductionTask productionTask : productionTasks) {
                if(productionTask.getSourceId().equals(productionPlanResult.getProductionPlanId())){
                    doneBomCount++;
                }
            }
            productionPlanResult.setDoneBomCount(doneBomCount);
            productionPlanResult.setWorkOrderResults(workOrderResultList);
            List<ProductionPlanDetailResult> results = new ArrayList<>();
            List<UserResult> userList = new ArrayList<>();
            if (ToolUtil.isNotEmpty(productionPlanResult.getUserId())) {
                for (Long userId : Arrays.stream(productionPlanResult.getUserId().split(",")).map(Long::parseLong).collect(Collectors.toList())) {
                    for (UserResult userResultsById : userResultsByIds) {
                        if (userId.equals(userResultsById.getUserId())) {
                            userList.add(userResultsById);
                            break;
                        }
                    }
                }
            }
            productionPlanResult.setUserList(userList);
            for (UserResult userResultsById : userResultsByIds) {


//                if (userResultsById.getUserId().equals(productionPlanResult.getUserId())) {
//                    productionPlanResult.setUserResult(userResultsById);
//                }
                if (userResultsById.getUserId().equals(productionPlanResult.getCreateUser())) {
                    productionPlanResult.setCreateUserResult(userResultsById);
                }
            }
            int number = 0 ;
            int receivedNumber = 0;
            for (ProductionPickListsResult productionPickListsResult : productionPlanList) {
                if (productionPickListsResult.getSourceId().equals(productionPlanResult.getProductionPlanId())){
                    number+= productionPickListsResult.getNumberCount();
                    receivedNumber+=productionPickListsResult.getReceivedCount();
                }
            }
            productionPlanResult.setNumberCount(number);
            productionPlanResult.setReceivedCount(receivedNumber);
            int skuCount = 0;
            for (ProductionPlanDetailResult detailResult : detailResults) {
                if (productionPlanResult.getProductionPlanId().equals(detailResult.getProductionPlanId())) {
                    skuCount+=detailResult.getSkuCount();
                    results.add(detailResult);
                }
            }
            productionPlanResult.setSkuCount(skuCount);
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
    public List<ProductionPlanResult> resultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionPlan> productionPlanList = this.listByIds(ids);
        List<ProductionPlanResult> productionPlanResults = BeanUtil.copyToList(productionPlanList, ProductionPlanResult.class, new CopyOptions());
        this.format(productionPlanResults);
        return productionPlanResults;
    }

}

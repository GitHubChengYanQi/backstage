package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.mapper.ProductionTaskMapper;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.request.JobBookingDetailCount;
import cn.atsoft.dasheng.production.model.request.SavePickListsObject;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 生产任务 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
@Service
public class ProductionTaskServiceImpl extends ServiceImpl<ProductionTaskMapper, ProductionTask> implements ProductionTaskService {
    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ProductionTaskDetailService productionTaskDetailService;

    @Autowired
    private ActivitiSetpSetDetailService activitiSetpSetDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductionJobBookingDetailService jobBookingDetailService;

    @Autowired
    private ProductionJobBookingService jobBookingService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ActivitiSetpSetService setpSetService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService partsDetailService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(ProductionTaskParam param) {
        ProductionWorkOrder productionWorkOrder = productionWorkOrderService.getById(param.getWorkOrderId());
        List<ActivitiSetpSetDetail> setpSetDetails = activitiSetpSetDetailService.query().eq("setps_id", productionWorkOrder.getStepsId()).eq("type", "out").list();
        ActivitiSetpSet setpSet = setpSetService.query().eq("setps_id", productionWorkOrder.getStepsId()).one();
        if(setpSet.getProductionType().equals("out")){
            List<Long> skuIds = new ArrayList<>();
            for (ActivitiSetpSetDetail setpSetDetail : setpSetDetails) {
                skuIds.add(setpSetDetail.getSkuId());
            }
            skuIds = skuIds.stream().distinct().collect(Collectors.toList());
            List<Parts> list = partsService.query().in("sku_id", skuIds).eq("display", 1).eq("status",99).list();
            if (list.size()!=skuIds.size()){
                throw new ServiceException(500,"有物料不存在Bom无法创建");
            }
        }

        /**
         * 判断是否满足库存
         */
        if (ToolUtil.isNotEmpty(param.getUserId())){
            checkStockDetail(param,productionWorkOrder);
        }



        /**
         * 判断拦截错误数据
         */
        List<ProductionTask> inTaskWorkOrder = this.query().eq("work_order_id", param.getWorkOrderId()).list();

        int count = 0;
        for (ProductionTask productionTask : inTaskWorkOrder) {
            count += productionTask.getNumber();
        }
        if (productionWorkOrder.getCount() < count + param.getNumber()) {
            throw new ServiceException(500, "不可分配多于工单数量的任务数量");
        }

        /**
         * 保存
         */
        ProductionTask entity = getEntity(param);
        if (ToolUtil.isNotEmpty(param.getUserIdList())) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Long userId : param.getUserIdList()) {
                stringBuffer.append(userId).append(",");
            }
            entity.setUserIds(stringBuffer.substring(0, stringBuffer.length() - 1));
        }
        entity.setShipSetpId(productionWorkOrder.getShipSetpId());
        this.save(entity);
        List<ProductionTaskDetail> detailEntitys = new ArrayList<>();
        /**
         * 保存子表信息
         * 从activitiSetpSetDetail表中取出产出物料信息
         * 然后与任务数量相乘
         * 保存进子表
         */

        for (ActivitiSetpSetDetail setpSetDetail : setpSetDetails) {
            ProductionTaskDetail productionTaskDetail = new ProductionTaskDetail();
            productionTaskDetail.setOutSkuId(setpSetDetail.getSkuId());
            productionTaskDetail.setProductionTaskId(entity.getProductionTaskId());
            productionTaskDetail.setNumber(setpSetDetail.getNum() * param.getNumber());
            if (ToolUtil.isNotEmpty(setpSetDetail.getQualityId())) {
                productionTaskDetail.setQualityId(setpSetDetail.getQualityId());
            }
            if (ToolUtil.isNotEmpty(setpSetDetail.getMyQualityId())) {
                productionTaskDetail.setMyQualityId(setpSetDetail.getMyQualityId());
            }

            detailEntitys.add(productionTaskDetail);
        }

        productionTaskDetailService.saveBatch(detailEntitys);


        /**
         * 发起任务
         */
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "productionTask").eq("status", 99).one();
        if (ToolUtil.isNotEmpty(activitiProcess) && ToolUtil.isNotEmpty(param.getUserId())) {
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的生产任务 (" + param.getCoding() + ")");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getProductionTaskId());
            activitiProcessTaskParam.setType("productionTask");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加铃铛
            wxCpSendTemplate.setSource("productionTask");
            wxCpSendTemplate.setSourceId(entity.getProductionTaskId());
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1);

        } else if (ToolUtil.isNotEmpty(param.getUserId())) {
            /**
             * 如果有审批则进行审批  没有直接推送微信消息
             */
            entity.setStatus(98);
            this.updateById(entity);
            WxCpTemplate wxCpTemplate = new WxCpTemplate();
            wxCpTemplate.setUrl(mobileService.getMobileConfig().getUrl()+"/#/Work/ProductionTask/Detail?id="+entity.getProductionTaskId().toString());
            wxCpTemplate.setTitle("新的生产任务");
            wxCpTemplate.setDescription("您被分派了新的生产任务" + entity.getCoding());
            wxCpTemplate.setUserIds(new ArrayList<Long>() {{
                add(entity.getUserId());
            }});
            wxCpSendTemplate.setSource("productionTask");
            wxCpSendTemplate.setSourceId(entity.getProductionTaskId());
            wxCpTemplate.setType(0);
            wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
            wxCpSendTemplate.sendTemplate();

            /**
             * 生成领料单
             */
            MicroServiceEntity serviceEntity = new MicroServiceEntity();
            serviceEntity.setType(MicroServiceType.PRODUCTION_PICKLISTS);
            serviceEntity.setOperationType(OperationType.ADD);
            String jsonString = JSON.toJSONString(new SavePickListsObject() {{
                setDetails(detailEntitys);
                setProductionTask(entity);
            }});
            serviceEntity.setObject(jsonString);
            serviceEntity.setMaxTimes(2);
            serviceEntity.setTimes(0);
            messageProducer.microService(serviceEntity);

        } else if (ToolUtil.isEmpty(param.getUserId())) {
            /**
             * 如果有审批则进行审批  没有直接推送微信消息
             */
            entity.setStatus(0);
            this.updateById(entity);
            WxCpTemplate wxCpTemplate = new WxCpTemplate();
            wxCpTemplate.setUrl(entity.getProductionTaskId().toString());
            wxCpTemplate.setTitle("新的生产任务");
            wxCpTemplate.setDescription("您被分派了新的生产任务" + entity.getCoding());
            wxCpTemplate.setUserIds(new ArrayList<Long>() {{
                add(entity.getUserId());
            }});
            wxCpSendTemplate.setSource("productionTask");
            wxCpSendTemplate.setSourceId(entity.getProductionTaskId());
            wxCpTemplate.setType(0);
            wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
            wxCpSendTemplate.sendTemplate();
        }

    }

    @Override
    public void delete(ProductionTaskParam param) {
        this.removeById(getKey(param));
    }

    private void checkStockDetail(ProductionTaskParam param, ProductionWorkOrder workOrder) {
        Long stepsId = workOrder.getStepsId();
        ActivitiSetpSet setpSet = setpSetService.query().eq("setps_id", stepsId).one();
        List<ActivitiSetpSetDetailResult> setpSetDetails = activitiSetpSetDetailService.getResultByStepsIds(new ArrayList<Long>() {{
            add(stepsId);
        }});
        if (setpSet.getProductionType().equals("in")) {

        } else if (setpSet.getProductionType().equals("out")) {
            List<Long> skuIds = new ArrayList<>();
            for (ActivitiSetpSetDetailResult setpSetDetail : setpSetDetails) {
                skuIds.add(setpSetDetail.getSkuId());
            }
            List<Long> partsIds = new ArrayList<>();
            List<Parts> parts = skuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skuIds).eq("display", 1).list();
            List<PartsResult> partsResults = new ArrayList<>();
            for (Parts part : parts) {
                partsIds.add(part.getPartsId());
                PartsResult partsResult = new PartsResult();
                ToolUtil.copyProperties(part,partsResult);
                partsResults.add(partsResult);
            }
            List<ErpPartsDetailResult> details = partsIds.size() == 0 ? new ArrayList<>() : partsDetailService.getDetails(partsIds);
            for (PartsResult partsResult : partsResults) {
                List<ErpPartsDetailResult> partsDetailResults = new ArrayList<>();
                for (ErpPartsDetailResult detail : details) {
                    if (partsResult.getPartsId().equals(detail.getPartsId())){
                        partsDetailResults.add(detail);
                    }
                }
                partsResult.setParts(partsDetailResults);
            }
            details.clear();
            for (ActivitiSetpSetDetailResult setpSetDetail : setpSetDetails) {
                for (PartsResult partsResult : partsResults) {
                    if (setpSetDetail.getSkuId().equals(partsResult.getSkuId())){
                        for (ErpPartsDetailResult part : partsResult.getParts()) {
                            part.setNumber(part.getNumber()*setpSetDetail.getNum());
                            details.add(part);
                        }
                    }
                }
            }

            details = details.stream().collect(Collectors.toMap(ErpPartsDetailResult::getSkuId, a -> a, (o1, o2) -> {
                o1.setNumber(o1.getNumber() + o2.getNumber());
                return o1;
            })).values().stream().collect(Collectors.toList());

            List<StockDetails> stockDetails = stockDetailsService.query().in("sku_id", skuIds).list();
            stockDetails = stockDetails.stream().collect(Collectors.toMap(StockDetails::getSkuId, a -> a, (o1, o2) -> {
                o1.setNumber(o1.getNumber() + o2.getNumber());
                return o1;
            })).values().stream().collect(Collectors.toList());
            if (stockDetails.size()==0){
                throw new ServiceException(500,"库存数量不足 不可以直接投入生产");
            }
            for (ErpPartsDetailResult detail : details) {
                for (StockDetails stockDetail : stockDetails) {
                    if (detail.getSkuId().equals(stockDetail.getSkuId()) && detail.getNumber()>stockDetail.getNumber()){
                        throw new ServiceException(500,"库存数量不足 不可以直接投入生产");
                    }
                }
            }
        }

    }

    @Override
    public ProductionTask update(ProductionTaskParam param) {
        ProductionTask oldEntity = getOldEntity(param);
        ProductionTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setDisplay(null);
        this.updateById(newEntity);
        return newEntity;
    }

    @Override
    public ProductionTask Receive(ProductionTaskParam param) {
        ProductionTask entity = this.getById(param.getProductionTaskId());
        ProductionWorkOrder productionWorkOrder = productionWorkOrderService.getById(entity.getWorkOrderId());
        checkStockDetail(param,productionWorkOrder);
        entity.setProductionTaskId(param.getProductionTaskId());
        entity.setUserId(param.getUserId());
        entity.setStatus(98);
        this.updateById(entity);
        /**
         * 创建任务单详情
         */
        List<ProductionTaskDetail> detailEntitys = new ArrayList<>();
        /**
         * 保存子表信息
         * 从activitiSetpSetDetail表中取出产出物料信息
         * 然后与任务数量相乘
         * 保存进子表
         */
        List<ActivitiSetpSetDetail> setpSetDetails = activitiSetpSetDetailService.query().eq("setps_id", productionWorkOrder.getStepsId()).eq("type", "out").list();

        for (ActivitiSetpSetDetail setpSetDetail : setpSetDetails) {
            ProductionTaskDetail productionTaskDetail = new ProductionTaskDetail();
            productionTaskDetail.setOutSkuId(setpSetDetail.getSkuId());
            productionTaskDetail.setProductionTaskId(entity.getProductionTaskId());
            productionTaskDetail.setNumber(setpSetDetail.getNum() * entity.getNumber());
            if (ToolUtil.isNotEmpty(setpSetDetail.getQualityId())) {
                productionTaskDetail.setQualityId(setpSetDetail.getQualityId());
            }
            if (ToolUtil.isNotEmpty(setpSetDetail.getMyQualityId())) {
                productionTaskDetail.setMyQualityId(setpSetDetail.getMyQualityId());
            }

            detailEntitys.add(productionTaskDetail);
        }

        productionTaskDetailService.saveBatch(detailEntitys);


        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(entity.getProductionTaskId().toString());
        wxCpTemplate.setTitle("新的生产任务");
        wxCpTemplate.setDescription("您领取了新的生产任务" + entity.getCoding());
        wxCpTemplate.setUserIds(new ArrayList<Long>() {{
            add(entity.getUserId());
        }});
        wxCpSendTemplate.setSource("productionTask");
        wxCpSendTemplate.setSourceId(entity.getProductionTaskId());
        wxCpTemplate.setType(0);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();

        MicroServiceEntity serviceEntity = new MicroServiceEntity();
        serviceEntity.setType(MicroServiceType.PRODUCTION_PICKLISTS);
        serviceEntity.setOperationType(OperationType.ADD);
        String jsonString = JSON.toJSONString(new SavePickListsObject() {{
            setDetails(detailEntitys);
            setProductionTask(entity);
        }});
        serviceEntity.setObject(jsonString);
        serviceEntity.setMaxTimes(2);
        serviceEntity.setTimes(0);
        messageProducer.microService(serviceEntity);

        return entity;

    }

    @Override
    public ProductionTaskResult findBySpec(ProductionTaskParam param) {
        return null;
    }

    @Override
    public List<ProductionTaskResult> findListBySpec(ProductionTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionTaskResult> findPageBySpec(ProductionTaskParam param) {
        Page<ProductionTaskResult> pageContext = getPageContext();
        IPage<ProductionTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<ProductionTaskResult> param) {
        List<Long> userIds = new ArrayList<>();
        List<Long> workOrderIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();

        for (ProductionTaskResult productionTaskResult : param) {
            taskIds.add(productionTaskResult.getProductionTaskId());
            /**
             * 添加工单id
             */
            workOrderIds.add(productionTaskResult.getWorkOrderId());
            /**
             * 把所有人员id添加list查询
             */
            if (ToolUtil.isNotEmpty(productionTaskResult.getUserId())) {
                userIds.add(productionTaskResult.getUserId());
            }
            if (ToolUtil.isNotEmpty(productionTaskResult.getUserIds())) {
                userIds.addAll(Arrays.stream(productionTaskResult.getUserIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            }
            userIds.add(productionTaskResult.getCreateUser());
        }
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        List<UserResult> userResults = userService.getUserResultsByIds(userIds);

        /**
         * 查询工单
         */
        List<ProductionWorkOrderResult> workOrderResults = productionWorkOrderService.resultsByIds(workOrderIds);
        /**
         * 查询子表以及报工表
         */
        List<ProductionTaskDetailResult> productionTaskDetailResults = productionTaskDetailService.resultsByTaskIds(taskIds);



        List<JobBookingDetailCount> counts = jobBookingDetailService.resultsByProductionTaskIds(taskIds);

        for (ProductionTaskResult productionTaskResult : param) {
            List<UserResult> userResultList = new ArrayList<>();
            for (UserResult userResult : userResults) {
                if (productionTaskResult.getCreateUser().equals(userResult.getUserId())) {
                    productionTaskResult.setCreateUserResult(userResult);
                }
                if (ToolUtil.isNotEmpty(productionTaskResult.getUserId()) && productionTaskResult.getUserId().equals(userResult.getUserId())) {
                    productionTaskResult.setUserResult(userResult);
                }

                if (ToolUtil.isNotEmpty(productionTaskResult.getUserIds())) {
                    List<Long> collect = Arrays.stream(productionTaskResult.getUserIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    for (Long aLong : collect) {
                        if (aLong.equals(userResult.getUserId())) {
                            userResultList.add(userResult);
                        }
                    }
                }

            }
            /**
             * 返回报工数量
             */
            List<ProductionTaskDetailResult> detailResults = new ArrayList<>();
            for (ProductionTaskDetailResult productionTaskDetailResult : productionTaskDetailResults) {
                if (productionTaskDetailResult.getProductionTaskId().equals(productionTaskResult.getProductionTaskId())) {
                    detailResults.add(productionTaskDetailResult);
                }
            }
            for (ProductionTaskDetailResult productionTaskDetailResult : detailResults) {
                for (JobBookingDetailCount count : counts) {
                    if (count.getSourceId().equals(productionTaskDetailResult.getProductionTaskId()) && count.getSkuId().equals(productionTaskDetailResult.getOutSkuId())) {
                        productionTaskDetailResult.setJobBookingDetailCount(count);
                    }
                }
            }
            productionTaskResult.setTaskDetailResults(detailResults);

            productionTaskResult.setUserResults(userResultList);
            /**
             * 匹配返回工单数据
             */
            for (ProductionWorkOrderResult workOrderResult : workOrderResults) {
                if (workOrderResult.getWorkOrderId().equals(productionTaskResult.getWorkOrderId())) {
                    productionTaskResult.setWorkOrderResult(workOrderResult);
                }
            }
        }

    }

    private Serializable getKey(ProductionTaskParam param) {
        return param.getProductionTaskId();
    }

    private Page<ProductionTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionTask getOldEntity(ProductionTaskParam param) {
        return this.getById(getKey(param));
    }

    private ProductionTask getEntity(ProductionTaskParam param) {
        ProductionTask entity = new ProductionTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<ProductionTaskResult> resultsByWorkOrderIds(List<Long> workOrderIds) {
        if (ToolUtil.isEmpty(workOrderIds) || workOrderIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionTask> productionTasks = this.query().in("work_order_id", workOrderIds).list();
        List<ProductionTaskResult> results = new ArrayList<>();
        for (ProductionTask productionTask : productionTasks) {
            ProductionTaskResult result = new ProductionTaskResult();
            ToolUtil.copyProperties(productionTask, result);
            results.add(result);
        }
        return results;
    }

    @Override
    public List<ProductionTaskResult> resultsByIds(List<Long> taskIds) {
        if (ToolUtil.isEmpty(taskIds) || taskIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionTask> productionTasks = this.listByIds(taskIds);
        List<ProductionTaskResult> results = new ArrayList<>();
        for (ProductionTask productionTask : productionTasks) {
            ProductionTaskResult result = new ProductionTaskResult();
            ToolUtil.copyProperties(productionTask, result);
            results.add(result);
        }
        return results;
    }

}

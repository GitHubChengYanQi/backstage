package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.app.model.result.InstockResult;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
import cn.atsoft.dasheng.erp.service.AnomalyService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.MaintenanceService;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessTaskMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.pojo.AppointUser;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.DataType;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;


/**
 * <p>
 * 流程任务表	 服务实现类
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
@Service
public class ActivitiProcessTaskServiceImpl extends ServiceImpl<ActivitiProcessTaskMapper, ActivitiProcessTask> implements ActivitiProcessTaskService {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiProcessLogService processLogService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private DocumentStatusService statusService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private ActivitiStepsService activitiStepsService;
    @Autowired
    private ActivitiAuditService activitiAuditService;
    @Autowired
    private ProductionPickListsService productionPickListsService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private MaintenanceService maintenanceService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private GetOrigin getOrigin;

    @Override
    public Long add(ActivitiProcessTaskParam param) {
        ActivitiProcessTask entity = getEntity(param);
        this.save(entity);
        String origin = this.getOrigin.newThemeAndOrigin("productionTask", entity.getProcessTaskId(), ToolUtil.isEmpty(param.getSource()) ? null : param.getSource(), ToolUtil.isEmpty(param.getSourceId()) ? null : param.getSourceId());
        entity.setOrigin(origin);
        this.updateById(entity);


        return entity.getProcessTaskId();

    }

    @Override
    public void delete(ActivitiProcessTaskParam param) {
        int admin = this.isAdmin(param.getProcessTaskId());
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        }
        ActivitiProcessTask newEntity = getEntity(param);
        newEntity.setDisplay(0);
        this.updateById(newEntity);
    }

    @Override
    public void update(ActivitiProcessTaskParam param) {
        int admin = this.isAdmin(param.getProcessTaskId());
        ActivitiProcessTask oldEntity = getOldEntity(param);
        ActivitiProcessTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行修改");
        }
        this.updateById(newEntity);
    }

    @Override
    public ActivitiProcessTask getByFormId(Long formId) {
        return this.baseMapper.selectOne(new QueryWrapper<ActivitiProcessTask>() {{
            eq("form_id", formId);
        }});

    }

    @Override
    public int isAdmin(Long taskId) {
        Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
        LoginContextHolder.getContext().getUserId();
        ActivitiProcessTask byId = this.getById(taskId);
        List<String> deptList = ToolUtil.isEmpty(byId.getDeptIds()) ? new ArrayList<>() : Arrays.asList(byId.getDeptIds().split(","));
        List<String> userList = ToolUtil.isEmpty(byId.getUserIds()) ? new ArrayList<>() : Arrays.asList(byId.getUserIds().split(","));
        boolean flag1 = deptList.contains(deptId.toString());
        boolean flag2 = userList.contains(deptId.toString());
        LoginContextHolder.getContext().isAdmin();
        int flag = 0;
        if (LoginContextHolder.getContext().isAdmin()) {
            flag = 1;
        } else if (!flag1 || !flag2) {
            flag = 0;
        } else {
            flag = 1;
        }
        return flag;
    }

    @Override
    public ActivitiProcessTaskResult findBySpec(ActivitiProcessTaskParam param) {
        return null;
    }

    @Override
    public List<ActivitiProcessTaskResult> findListBySpec(ActivitiProcessTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiProcessTaskResult> findPageBySpec(ActivitiProcessTaskParam param) {


        Page<ActivitiProcessTaskResult> pageContext = getPageContext();
        IPage<ActivitiProcessTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public ActivitiProcessTaskResult detail(Long id) {
        ActivitiProcessTask processTask = this.getById(id);
        ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
        ToolUtil.copyProperties(processTask, taskResult);

        format(new ArrayList<ActivitiProcessTaskResult>() {{
            add(taskResult);
        }});
        return taskResult;
    }

    @Override
    public PageInfo<ActivitiProcessTaskResult> auditList(ActivitiProcessTaskParam param) {

        List<Long> taskId = getTaskId();    //查看权限
        if (ToolUtil.isEmpty(taskId)) {
            taskId = new ArrayList<>();
            taskId.add(0L);
        }
        param.setTaskIds(taskId);
        Long userId = LoginContextHolder.getContext().getUserId();
        param.setUserIds(userId.toString());


        Page<ActivitiProcessTaskResult> pageContext = getPageContext();
        IPage<ActivitiProcessTaskResult> page = this.baseMapper.auditList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);

    }

    @Override
    public PageInfo<ActivitiProcessTaskResult> LoginStart(ActivitiProcessTaskParam param) {
        Page<ActivitiProcessTaskResult> pageContext = getPageContext();
        IPage<ActivitiProcessTaskResult> page = this.baseMapper.auditList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiProcessTaskParam param) {
        return param.getProcessTaskId();
    }

    private Page<ActivitiProcessTaskResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
        }};
        return PageFactory.defaultPage(fields);
    }

    private ActivitiProcessTask getOldEntity(ActivitiProcessTaskParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcessTask getEntity(ActivitiProcessTaskParam param) {
        ActivitiProcessTask entity = new ActivitiProcessTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    private List<Long> getTaskId() {
        List<Long> taskIds = new ArrayList<>();
        List<Long> stepIds = getStepIdsByType();
        List<ActivitiProcessLog> processLogList = stepIds.size() == 0 ? new ArrayList<>() : processLogService.query()
                .in("setps_id", stepIds)
                .groupBy("task_id").list();
        for (ActivitiProcessLog activitiProcessLog : processLogList) {
            taskIds.add(activitiProcessLog.getTaskId());
        }
        return taskIds;
    }

    /**
     * 查出当前下关于用户所有步骤
     *
     * @param
     * @return
     */
    List<Long> getStepIdsByType() {
        LoginContext context = LoginContextHolder.getContext();

        List<ActivitiAudit> audits = auditService.query().eq("display", 1).list();
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiAudit audit : audits) {
            AuditRule rule = audit.getRule();
            if (ToolUtil.isNotEmpty(rule) && ToolUtil.isNotEmpty(rule.getType()) && haveME(rule, context)) {
                stepIds.add(audit.getSetpsId());
            }
        }
        return stepIds;
    }


    /**
     * 当前规则指定人
     *
     * @param rule
     * @return
     */
    private boolean haveME(AuditRule rule, LoginContext loginContext) {
        LoginUser user = loginContext.getUser();
        List<Long> depts = loginContext.getDeptDataScope();
        if (ToolUtil.isEmpty(user.getId())) {
            return false;
        }
        for (AuditRule.Rule ruleRule : rule.getRules()) {
            if (ToolUtil.isNotEmpty(ruleRule.getDeptPositions())) {
                for (Long dept : depts) {
                    if (ruleRule.getDeptPositions().stream().anyMatch(i -> i.getKey().equals(dept.toString()))) {
                        return true;
                    }
                }
            }

            if (ruleRule.getType().equals(DataType.AllPeople)) {
                return false;
            }
            for (AppointUser appointUser : ruleRule.getAppointUsers()) {
                if (appointUser.getKey().equals(user.getId().toString())) {
                    return true;
                }
            }

        }

        return false;
    }


    /**
     * 当前规则指定人
     *
     * @param rule
     * @return
     */
    private boolean startHaveME(AuditRule rule, LoginContext loginContext) {
        LoginUser user = loginContext.getUser();
        List<Long> depts = loginContext.getDeptDataScope();
        if (ToolUtil.isEmpty(user.getId())) {
            return false;
        }
        for (AuditRule.Rule ruleRule : rule.getRules()) {
            if (ToolUtil.isNotEmpty(ruleRule.getDeptPositions())) {
                for (Long dept : depts) {
                    if (ruleRule.getDeptPositions().stream().anyMatch(i -> i.getKey().equals(dept.toString()))) {
                        return true;
                    }
                }
            }

            if (ruleRule.getType().equals(DataType.AllPeople)) {
                return true;
            }
            for (AppointUser appointUser : ruleRule.getAppointUsers()) {
                if (appointUser.getKey().equals(user.getId().toString())) {
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public Long getTaskIdByFormId(Long formId) {
        ActivitiProcessTask task = ToolUtil.isEmpty(formId) ? new ActivitiProcessTask() : this.query().eq("form_id", formId).one();
        return task.getProcessTaskId();
    }

    @Override
    public void format(List<ActivitiProcessTaskResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> instockOrderIds = new ArrayList<>();
        List<Long> anomalyOrderIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> inventoryIds = new ArrayList<>();
        List<Long> anomalyIds = new ArrayList<>();
        List<Long> maintenanceIds = new ArrayList<>();
        for (ActivitiProcessTaskResult datum : data) {
            userIds.add(datum.getCreateUser());
            switch (datum.getType()) {
                case "INSTOCK":
                    instockOrderIds.add(datum.getFormId());
                    break;
                case "ERROR":
                    anomalyOrderIds.add(datum.getFormId());
                    break;
                case "OUTSTOCK":
                    pickListsIds.add(datum.getFormId());
                    break;
                case "ErrorForWard":   //异常转交处理
                    anomalyIds.add(datum.getFormId());
                case "Stocktaking":
                    inventoryIds.add(datum.getFormId());
                    break;
                case "MAINTENANCE":
                    maintenanceIds.add(datum.getFormId());
                    break;

            }
        }
        Map<Long, String> statusMap = new HashMap<>();
        List<DocumentsStatus> statuses = statusService.list();
        statusMap.put(0L, "开始");
        statusMap.put(99L, "完成");
        statusMap.put(50L, "拒绝");

        for (DocumentsStatus status : statuses) {
            statusMap.put(status.getDocumentsStatusId(), status.getName());
        }

        List<InstockOrder> instockOrders = instockOrderIds.size() == 0 ? new ArrayList<>() : instockOrderService.listByIds(instockOrderIds);
        List<InstockOrderResult> orderResults = BeanUtil.copyToList(instockOrders, InstockOrderResult.class, new CopyOptions());
        instockOrderService.format(orderResults);
        instockOrderService.setList(orderResults);

        List<AnomalyOrder> anomalyOrders = anomalyOrderIds.size() == 0 ? new ArrayList<>() : anomalyOrderService.listByIds(anomalyOrderIds);
        List<AnomalyOrderResult> orderResultList = BeanUtil.copyToList(anomalyOrders, AnomalyOrderResult.class, new CopyOptions());
        anomalyOrderService.format(orderResultList);

        List<ProductionPickLists> productionPickLists = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsIds);
        List<ProductionPickListsResult> productionPickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
        List<MaintenanceResult> maintenanceResults = maintenanceIds.size() == 0 ? new ArrayList<>() : maintenanceService.resultsByIds(maintenanceIds);

        List<Anomaly> anomalies = anomalyIds.size() == 0 ? new ArrayList<>() : anomalyService.listByIds(anomalyIds);
        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        anomalyService.getOrder(anomalyResults);
        anomalyService.format(anomalyResults);

        List<Inventory> inventories = inventoryIds.size() == 0 ? new ArrayList<>() : inventoryService.listByIds(inventoryIds);
        List<InventoryResult> inventoryResults = BeanUtil.copyToList(inventories, InventoryResult.class, new CopyOptions());
        inventoryService.format(inventoryResults);

        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        for (ActivitiProcessTaskResult datum : data) {

            for (User user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    datum.setUser(user);
                    break;
                }
            }

            for (InstockOrderResult orderResult : orderResults) {
                if (datum.getType().equals("INSTOCK") && datum.getFormId().equals(orderResult.getInstockOrderId())) {
                    String statusName = statusMap.get(orderResult.getStatus());
                    orderResult.setStatusName(statusName);
                    datum.setReceipts(orderResult);
                    break;
                }
            }

            for (AnomalyOrderResult anomalyOrderResult : orderResultList) {
                if (datum.getType().equals("ERROR") && datum.getFormId().equals(anomalyOrderResult.getOrderId())) {
                    String statusName = statusMap.get(anomalyOrderResult.getStatus());
                    anomalyOrderResult.setStatusName(statusName);
                    datum.setReceipts(anomalyOrderResult);
                    break;
                }
            }
            for (ProductionPickListsResult productionPickListsResult : productionPickListsResults) {
                if (datum.getType().equals("OUTSTOCK") && datum.getFormId().equals(productionPickListsResult.getPickListsId())) {
                    String statusName = statusMap.get(productionPickListsResult.getStatus());
                    productionPickListsResult.setStatusName(statusName);
                    productionPickListsService.taskFormat(new ArrayList<ProductionPickListsResult>() {{
                        add(productionPickListsResult);
                    }});
                    datum.setReceipts(productionPickListsResult);
                    break;
                }
            }

            for (InventoryResult inventoryResult : inventoryResults) {
                if (inventoryResult.getInventoryTaskId().equals(datum.getFormId())) {
                    String statusName = statusMap.get(inventoryResult.getStatus());
                    inventoryResult.setStatusName(statusName);
                    datum.setReceipts(inventoryResult);
                    break;
                }
            }
            for (AnomalyResult anomalyResult : anomalyResults) {
                if (datum.getType().equals("ErrorForWard") && datum.getFormId().equals(anomalyResult.getAnomalyId())) {
                    datum.setReceipts(anomalyResult);

                }
            }
            for (MaintenanceResult maintenanceResult : maintenanceResults) {
                if (datum.getType().equals("MAINTENANCE") && datum.getFormId().equals(maintenanceResult.getMaintenanceId())) {
                    String statusName = statusMap.get(maintenanceResult.getStatus());
                    maintenanceResult.setStatusName(statusName);
                    datum.setReceipts(maintenanceResult);
                }
            }
        }

    }
    @Override
    public Map<String,String> getSendData(Long taskId) {
        ActivitiProcessTask processTask = this.getById(taskId);
        Long processId = processTask.getProcessId();
        ActivitiProcess activitiProcess = activitiProcessService.getById(processId);
        String modelName = ProcessModuleEnum.getModelNameByEnum(activitiProcess.getType());
        Map<String,String> result = new HashMap<>();
        result.put("function",modelName);
        String coding = "";
        List<SkuSimpleResult> skuSimpleResults = new ArrayList<>();
        try {
            switch (processTask.getType()) {
//                case "quality_task":
//                    QualityTaskResult task = qualityTaskService.getTask(taskResult.getFormId());
//                    break;
//                case "purchase":
//                case "purchaseAsk":
//                    PurchaseAskParam param = new PurchaseAskParam();
//                    param.setPurchaseAskId(taskResult.getFormId());
//                    PurchaseAskResult askResult = askService.detail(param);
//                    taskResult.setObject(askResult);
//                    break;
//                case "procurementOrder":
//                    ProcurementOrder detail = this.procurementOrderService.getById(taskResult.getFormId());
//                    ProcurementOrderResult result = new ProcurementOrderResult();
//                    ToolUtil.copyProperties(detail, result);
//                    User user1 = userService.getById(result.getCreateUser());
//                    result.setUser(user1);
//                    taskResult.setObject(result);
//
//                    break;
//                case "purchasePlan":
//                    ProcurementPlan procurementPlan = this.procurementPlanService.getById(taskResult.getFormId());
//                    if (ToolUtil.isEmpty(procurementPlan)) {
//                        return null;
//                    }
//                    ProcurementPlanResult procurementPlanResult = new ProcurementPlanResult();
//                    ToolUtil.copyProperties(procurementPlan, procurementPlanResult);
//                    User user = userService.getById(procurementPlanResult.getCreateUser());
//                    procurementPlanResult.setFounder(user);
//                    procurementPlanService.detail(procurementPlanResult);
//                    taskResult.setObject(procurementPlanResult);
//                    break;
                case "INSTOCK":
                    InstockOrderResult instockOrderResult = BeanUtil.copyProperties(instockOrderService.getById(processTask.getFormId()), InstockOrderResult.class);
                    instockOrderService.formatDetail(instockOrderResult);
                    for (InstockListResult instockListResult : instockOrderResult.getInstockListResults()) {
                        skuSimpleResults.add(BeanUtil.copyProperties(instockListResult.getSkuResult(),SkuSimpleResult.class));
                    }
                    coding  = instockOrderResult.getCoding();
                    break;
                case "ERROR":
                    AnomalyOrderResult orderResult = anomalyOrderService.detail(processTask.getFormId());
                    for (AnomalyResult anomalyResult : orderResult.getAnomalyResults()) {
                        skuSimpleResults.add(anomalyResult.getSkuResult());
                    }
                    coding = orderResult.getCoding();
                    break;
                case "ErrorForWard":
                    AnomalyResult anomalyResult = anomalyService.detail(processTask.getFormId());
                    skuSimpleResults.add(anomalyResult.getSkuResult());
                    break;
                case "OUTSTOCK":
                    ProductionPickListsResult pickListsRestult = pickListsService.detail(processTask.getFormId());
                    for (ProductionPickListsDetailResult detailResult : pickListsRestult.getDetailResults()) {
                        skuSimpleResults.add(detailResult.getSkuResult());
                    }
                    coding  = pickListsRestult.getCoding();
                    break;
                case "MAINTENANCE":
                    MaintenanceResult maintenanceResult = maintenanceService.detail(processTask.getFormId());
                    for (MaintenanceDetailResult maintenanceDetailResult : maintenanceResult.getMaintenanceDetailResults()) {
                        skuSimpleResults.add(maintenanceDetailResult.getSkuResult());
                    }
                    coding  = maintenanceResult.getCoding();
                    break;
                case "Stocktaking":
                    InventoryResult inventoryResult = inventoryService.detail(processTask.getFormId());
                    for (InventoryDetailResult detailResult : inventoryResult.getDetailResults()) {
                        skuSimpleResults.add(BeanUtil.copyProperties(detailResult.getSkuResult(), SkuSimpleResult.class));
                    }
                    coding  = inventoryResult.getCoding();

                    break;


            }
        } catch (Exception e) {

        }
        StringBuffer stringBuffer = new StringBuffer();
        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
            if (ToolUtil.isNotEmpty(skuSimpleResult.getSpuResult()) && ToolUtil.isNotEmpty(skuSimpleResult.getSpuResult().getName())) {
                stringBuffer.append(skuSimpleResult.getSpuResult().getName()).append("/");
            }
            if (ToolUtil.isNotEmpty(skuSimpleResult.getSkuName())) {
                stringBuffer.append(skuSimpleResult.getSkuName()).append("/");

            }
            if (ToolUtil.isNotEmpty(skuSimpleResult.getSpecifications())) {
                stringBuffer.append(skuSimpleResult.getSpecifications());
            }
            stringBuffer.append(",");
            if (stringBuffer.length()>20){
                stringBuffer.append(".....");

                break;
            }
        }
        result.put("description",stringBuffer.toString());
        result.put("coding",coding);
        return result;
    }

    @Override
    public void checkStartUser(Long processId) {

        List<ActivitiSteps> steps = activitiStepsService.query().eq("process_id", processId).eq("display", 1).list();

        List<ActivitiAudit> audits = activitiAuditService.getListBySteps(steps);
        for (ActivitiAudit audit : audits) {
            if (audit.getType().equals("start")) {
                if (!startHaveME(audit.getRule(), LoginContextHolder.getContext())) {
                    throw new ServiceException(500, "您没有创建流程发起权限");
                }
            }
        }

    }
}





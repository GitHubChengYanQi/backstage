package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.action.Enum.*;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.auditView.service.AuditViewService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.erp.service.impl.CheckInstock;
import cn.atsoft.dasheng.erp.service.impl.CheckQualityTask;
import cn.atsoft.dasheng.erp.service.impl.ProcessTaskEndSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.*;
import cn.atsoft.dasheng.form.pojo.*;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.impl.ProductionPickListsServiceImpl;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.*;
import cn.atsoft.dasheng.purchase.service.impl.CheckPurchaseAsk;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.ProcessType.ALLOCATION;


/**
 * <p>
 * 流程日志表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiProcessLogServiceV1Impl extends ServiceImpl<ActivitiProcessLogMapper, ActivitiProcessLog> implements ActivitiProcessLogV1Service {

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiProcessTaskSend taskSend;

    @Autowired
    private CheckQualityTask checkQualityTask;

    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private GetOrigin getOrigin;


    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private CheckPurchaseAsk checkPurchaseAsk;

    @Autowired
    private ProcessTaskEndSend endSend;

    @Autowired
    private PurchaseAskService askService;

    @Autowired
    private RemarksService remarksService;

    @Autowired
    private AuditViewService viewService;

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private PurchaseAskService purchaseAskService;

    @Autowired
    private ProcurementOrderService procurementOrderService;
    @Autowired
    private InquiryTaskService inquiryTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private CheckInstock checkInstock;

    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private DocumentsActionService documentsActionService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private ActivitiProcessService processService;

    @Autowired
    private ProductionPickListsCartService pickListsCartService;
    @Autowired
    private AllocationCartService allocationCartService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private SkuService skuService;


    @Override
    public ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId) {
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
    }

    @Override
    public List<ActivitiProcessLog> listByTaskId(Long taskId) {
        return this.list(new QueryWrapper<ActivitiProcessLog>() {{
            eq("task_id", taskId);
        }});
    }

    @Override
    public void audit(Long taskId, Integer status) {
//        //判断采购申请状态
//        askService.updateStatus(taskId, status);
        Long loginUserId = LoginContextHolder.getContext().getUserId();
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isNotEmpty(task.getVersion()) && task.getVersion().equals(1)) {
            this.auditPerson(taskId, status, loginUserId);
        } else {
            activitiProcessLogService.audit(taskId, status);
        }
    }

    @Override
    public void autoAudit(Long taskId, Integer status, Long loginUserId) {
        if (ToolUtil.isEmpty(status)) {
            status = 1;
        }
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isNotEmpty(task.getVersion()) && task.getVersion().equals(1)) {
            this.auditPerson(taskId, status, loginUserId);
        } else {
            activitiProcessLogService.audit(taskId, status);
        }
    }


    private void setStatus(List<ActivitiProcessLog> logs, Long logId,Integer status) {
        for (ActivitiProcessLog activitiProcessLog : logs) {
            if (logId.equals(activitiProcessLog.getLogId())) {
                activitiProcessLog.setStatus(status);
            }
        }
    }

    public void auditPerson(Long taskId, Integer status, Long loginUserId) {
        if (ToolUtil.isEmpty(status)) {
            throw new ServiceException(500, "请填写审核状态");
        }
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "没有找到该任务，无法进行审批");
        }

        List<ActivitiProcessLog> logs = listByTaskId(taskId);
        List<ActivitiProcessLog> audit = this.getAudit3(taskId);
        if (audit.size() == 0) {
            audit = this.getAudit1(taskId);
        }
        /**
         * 流程中审核节点
         */
        List<Long> stepsIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : logs) {
            stepsIds.add(processLog.getSetpsId());
        }
        if (ToolUtil.isEmpty(stepsIds)) {
            return;
        }
        /**
         * 取出所有步骤配置
         */
        List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepsIds.stream().distinct().collect(Collectors.toList()));
        }});

        List<Long> allStepsByLog = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : logs) {
            allStepsByLog.add(activitiProcessLog.getSetpsId());
        }
        List<ActivitiSteps> allSteps = stepsService.listByIds(allStepsByLog);

        /**
         * 是否审批通过flag
         * 如果为false 则不向下进行推送
         */
        boolean auditCheck = true;

        //循环流程下步骤
        for (ActivitiProcessLog activitiProcessLog : audit) {
            if (ToolUtil.isEmpty(activitiProcessLog.getAuditUserId()) || loginUserId.equals(activitiProcessLog.getAuditUserId())) {
                /**
                 * 取节点规则
                 */
                ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());

                /**
                 * 取log步骤
                 */
                ActivitiSteps activitiSteps = getSteps(allSteps, activitiProcessLog.getSetpsId());

                if (ToolUtil.isEmpty(activitiAudit) || ToolUtil.isEmpty(activitiSteps)) {
                    continue;
                }
                if (activitiAudit.getType().equals("branch") || activitiAudit.getType().equals("route")) {
                    continue;
                }
                RuleType auditType = activitiAudit.getRule().getType();

                /**
                 * 判断是否审批节点 不是则自动通过  是则看传入参数
                 */
                if (!"audit".equals(auditType.toString())) {
                    if (activitiAudit.getType().equals("process")) {
                        switch (task.getType()) {
                            case "quality_task":
                                if (checkQualityTask.checkTask(task.getFormId(), activitiAudit.getRule().getType())) {
                                    //更新状态
                                    updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                                    setStatus(logs, activitiProcessLog.getLogId(),status);
                                    //拒绝走拒绝方法
                                    if (status.equals(0)) {
                                        this.refuseTask(task);
                                        auditCheck = false;
                                    }
                                } else {
                                    auditCheck = false;
                                }
                                break;

                            case "purchaseAsk":
                                if (checkPurchaseAsk.checkTask(task.getFormId(), activitiAudit.getRule().getType())) {
                                    updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                                    setStatus(logs, activitiProcessLog.getLogId(),status);
                                    //拒绝走拒绝方法
                                    if (status.equals(0)) {
                                        this.refuseTask(task);
                                        auditCheck = false;
                                    }
                                } else {
                                    auditCheck = false;
                                }
                                break;
                            case "ERROR":   //入库异常
                                if (checkInstock.checkTask(task.getFormId(), activitiAudit.getRule().getType())) {
                                    updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                                    setStatus(logs, activitiProcessLog.getLogId(),status);
                                    //拒绝走拒绝方法
                                    if (status.equals(0)) {
                                        this.refuseTask(task);
                                        auditCheck = false;
                                    }
                                } else {
                                    auditCheck = false;
                                }
                                break;
                            case "createInstock":   //入库创建
                            case "INSTOCK":   //入库创建
                            case "OUTSTOCK":   //出库
                            case "Stocktaking":
                            case "MAINTENANCE":
                            case "ALLOCATION":
                                updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                                setStatus(logs, activitiProcessLog.getLogId(),status);
                                //拒绝走拒绝方法
                                if (status.equals(0)) {
                                    this.refuseTask(task);
                                    auditCheck = false;
                                }

                                break;

                            default:

                        }
                    } else {
                        updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                        setStatus(logs, activitiProcessLog.getLogId(),status);
                    }
                } else {
                    //判断权限  筛选对应log
                    if (this.checkUser(activitiAudit.getRule(), loginUserId, taskId)) {
                        updateStatus(activitiProcessLog.getLogId(), taskId, status, loginUserId);
                        setStatus(logs, activitiProcessLog.getLogId(),status);
                        //判断审批是否通过  不通过推送发起人审批状态  通过 在方法最后发送下一级执行
                        if (status.equals(0)) {
                            this.refuseTask(task);
                            auditCheck = false;
                        }
                    }
                }

                /**
                 * 更新当前线路的所有节点
                 */
                List<ActivitiProcessLog> processLogs = new ArrayList<>();
                for (ActivitiSteps step : allSteps) {
                    if (activitiProcessLog.getSetpsId().toString().equals(step.getSetpsId().toString())) {
                        processLogs = updateSupper(allSteps, logs, step,loginUserId);
                    }
                }
                for (ActivitiProcessLog processLog : processLogs) {
                    processLog.setStatus(status);
                }

                AuditRule rule = activitiAudit.getRule();
                if ((ToolUtil.isNotEmpty(rule) && ToolUtil.isNotEmpty(rule.getNodeApprovalType()) && rule.getNodeApprovalType().equals(1)) || ToolUtil.isEmpty(rule.getNodeApprovalType())) {
                    for (ActivitiProcessLog processLog : logs) {
                        if (processLog.getStatus().equals(3)) {
                            processLog.setStatus(4);
                            processLogs.add(processLog);
                        }
                    }
                }
                this.updateBatchById(processLogs);


            }
        }


        /**
         * 自动审批 抄送节点以及判断上级更新上级路由与分支
         */
        loopNext(task, activitiAudits, allSteps, auditCheck, loginUserId);
        /**
         * 流程结束需要重新获取需要审批的节点
         */
        audit = this.getAudit1(taskId);
        if (audit.size() == 0) {
            audit.addAll(this.getAudit3(taskId));
        }
        // 写一个判断如果下步为动作时 执行动作
//        startAction(audit, task);
        /**
         * 更新单据状态
         */
        if (auditCheck) {
            for (ActivitiProcessLog processLog : audit) {
                processLog.setStatus(3);
            }
            this.updateBatchById(audit);
            updateDocumentStatus(audit, activitiAudits, task);
        }


        //如果查询下级需要审批节点  为空  则审批流程已经没有了 下级节点  则流程结束
        if (ToolUtil.isEmpty(audit)) {
            /**
             * 流程结束
             */
            //如果上级审批为 同意（通过）则更新状态为完成
            //否则 在上面代码中  审核时已经更改单据和审批流程为否决 不再次更改
            if (auditCheck) {

                ActivitiProcessTask endProcessTask = new ActivitiProcessTask();
                ToolUtil.copyProperties(task, endProcessTask);
                endProcessTask.setStatus(99);
                //更新任务状态
                activitiProcessTaskService.updateById(endProcessTask);
                //更新任务关联单据状态
                this.updateStatus(task);
                /**
                 * 如果是子单据  子单据完成  反向去更新父级状态
                 */
                this.updateParentProcessTask(task, loginUserId);
            }
            //推送流程结束消息
            endSend.endSend(task.getProcessTaskId());
        }
    }


    private void updateDocumentStatus(List<ActivitiProcessLog> logs, List<ActivitiAudit> activitiAudits, ActivitiProcessTask task) {
        /**
         * 把所有过程（非 开始、路由、分支）
         */
        List<ActivitiAudit> process = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : logs) {
            for (ActivitiAudit activitiAudit : activitiAudits) {
                if (activitiProcessLog.getSetpsId().equals(activitiAudit.getSetpsId()) && activitiAudit.getType().equals("process")) {
                    process.add(activitiAudit);
                }
            }
        }

        if (ToolUtil.isNotEmpty(process) && ToolUtil.isNotEmpty(process.get(0).getDocumentsStatusId())) {
            Long documentsStatusId = process.get(0).getDocumentsStatusId();
            Long formId = task.getFormId();
            String type = task.getType();
            switch (type) {
                case "quality_task":
                    QualityTask qualityTask = qualityTaskService.getById(formId);
                    qualityTask.setStatus(documentsStatusId);
                    qualityTaskService.updateById(qualityTask);
                    break;
                case "purchaseAsk":
                    PurchaseAsk purchaseAsk = purchaseAskService.getById(formId);
                    purchaseAsk.setStatus(documentsStatusId);
                    purchaseAskService.updateById(purchaseAsk);
                    break;
                case "procurementOrder":
                    ProcurementOrder procurementOrder = procurementOrderService.getById(formId);
                    procurementOrder.setStatus(documentsStatusId);
                    procurementOrderService.updateById(procurementOrder);
                    break;
                case "purchasePlan":
                    break;
                case "INSTOCK":
                    InstockOrder instockOrder = instockOrderService.getById(formId);
                    instockOrder.setStatus(documentsStatusId);
                    instockOrderService.updateById(instockOrder);
                    break;
                case "ERROR":
                    AnomalyOrder anomalyOrder = anomalyOrderService.getById(formId);
                    anomalyOrder.setStatus(documentsStatusId);
                    anomalyOrderService.updateById(anomalyOrder);
                    break;
                case "OUTSTOCK":
                    ProductionPickLists productionPickLists = pickListsService.getById(formId);
                    productionPickLists.setStatus(documentsStatusId);
                    pickListsService.updateById(productionPickLists);
                    break;
                case "Stocktaking":
                    Inventory inventory = inventoryService.getById(formId);
                    inventory.setStatus(documentsStatusId);
                    inventoryService.updateById(inventory);
                    break;
            }

        }
    }

    //
    private void updateStatus(ActivitiProcessTask processTask) {
        switch (processTask.getType()) {
            case "purchasePlan":
                procurementPlanService.updateState(processTask);
                break;
            case "inQuality":
            case "outQuality":
                break;
            case "purchaseAsk":
                purchaseAskService.updateStatus(processTask);
                break;
            case "procurementOrder":
                procurementOrderService.updateStatus(processTask);
                break;
            case "inQuiry":
            case "inquiry":
                inquiryTaskService.updateStatus(processTask);
                break;
            case "ERROR":
                anomalyOrderService.updateStatus(processTask);
                break;
            case "INSTOCK":
                instockOrderService.updateCreateInstockStatus(processTask);
                break;
            case "Stocktaking":
                inventoryService.updateStatus(processTask);
                break;
            case "OUTSTOCK":
                pickListsService.updateStatus(processTask);
                break;
        }
    }

    private void updateParentProcessTask(ActivitiProcessTask processTask, Long loginUserId) {
        ThemeAndOrigin origin = new ThemeAndOrigin();
        switch (processTask.getType()) {
            case "outQuality":
            case "inQuality":
                QualityTask qualityTask = qualityTaskService.getById(processTask.getFormId());
                if (ToolUtil.isNotEmpty(qualityTask.getOrigin())) {
                    origin = getOrigin.getOrigin(JSON.parseObject(qualityTask.getOrigin(), ThemeAndOrigin.class));
                    if (ToolUtil.isNotEmpty(origin.getParent())) {
                        for (ThemeAndOrigin themeAndOrigin : origin.getParent()) {
                            if (themeAndOrigin.getSource().equals("processTask")) {
                                //如果来源存的是流程任务的id
                                ActivitiProcessTask parentProcessTask = activitiProcessTaskService.getById(themeAndOrigin.getSourceId());

                                checkAction(parentProcessTask.getProcessId(), ReceiptsEnum.QUALITY.name(), QualityActionEnum.done.getStatus(), loginUserId);
                            } else {
                                //如果来源存的是主单据的id
                                checkAction(themeAndOrigin.getSourceId(), ReceiptsEnum.QUALITY.name(), QualityActionEnum.done.getStatus(), loginUserId);
                            }
                        }
                    }
                }
                break;
            case "purchaseInstock":
                break;

            case "INSTOCK":
                InstockOrder instockOrder = instockOrderService.getById(processTask.getFormId());
                if (ToolUtil.isNotEmpty(processTask.getSource())) {
                    switch (processTask.getSource()) {
                        /**
                         * 如果说 入库单来源是调拨  那么 需要检查调拨 任务是否完成
                         */
                        case "ALLOCATION":
                            List<InstockList> instockLists = instockListService.query().eq("instock_order_id", instockOrder.getInstockOrderId()).eq("display", 1).list();
                            allocationService.checkCartDone(processTask.getPid(), instockLists);
                            skuService.skuMessage(instockLists.get(0).getSkuId());
                            break;
                        default:

                    }
                }
//                if (ToolUtil.isNotEmpty(instockOrderByid.getOrigin())) {
//                    origin = getOrigin.getOrigin(JSON.parseObject(instockOrderByid.getOrigin(), ThemeAndOrigin.class));
//                    if (ToolUtil.isNotEmpty(origin.getParent())) {
//                        for (ThemeAndOrigin themeAndOrigin : origin.getParent()) {
//                            if (themeAndOrigin.getSource().equals("processTask")) {
//                                //如果来源存的是流程任务的id
//                                ActivitiProcessTask parentProcessTask = activitiProcessTaskService.getById(themeAndOrigin.getSourceId());
//                                List<ActivitiProcessTask> list = activitiProcessTaskService.query().eq("source", "processTask").eq("source_id", parentProcessTask.getProcessTaskId()).list();
//                                if (list.stream().allMatch(i -> i.getStatus().equals(99))) {
//                                    checkAction(parentProcessTask.getProcessId(), ReceiptsEnum.INSTOCK.name(), InStockActionEnum.done.getStatus(), loginUserId);
//                                }
//                            }
//                            else {
//                                ActivitiProcessTask parentProcessTask = activitiProcessTaskService.query().eq("type", themeAndOrigin.getSource()).eq("form_id", themeAndOrigin.getSourceId()).one();
//                                List<ActivitiProcessTask> list = activitiProcessTaskService.query().eq("source", "processTask").eq("source_id", parentProcessTask.getProcessTaskId()).list();
//                                if (list.stream().allMatch(i -> i.getStatus().equals(99))) {
//                                    checkAction(parentProcessTask.getProcessId(), ReceiptsEnum.INSTOCK.name(), InStockActionEnum.done.getStatus(), loginUserId);
//                                }
//                            }
//                        }
//                    }
//                }
                break;
            case "ERROR":
                List<Anomaly> anomalies = anomalyService.lambdaQuery().eq(Anomaly::getOrderId, processTask.getFormId()).list();
                if (ToolUtil.isNotEmpty(anomalies)) {
                    Anomaly anomaly = anomalies.get(0);
                    if (anomaly.getType().equals("InstockError")) {
                        boolean b = instockOrderService.instockOrderComplete(anomaly.getFormId());
                        if (b) {
                            ActivitiProcessTask task = activitiProcessTaskService.lambdaQuery().eq(ActivitiProcessTask::getFormId, anomaly.getFormId()).one();
                            autoAudit(task.getProcessTaskId(), 1, loginUserId);
                        }
                    }

                }

                break;

            case "OUTSTOCK":
                ProductionPickLists pickLists = pickListsService.getById(processTask.getFormId());
                if (ToolUtil.isNotEmpty(pickLists.getSource()) && pickLists.getSource().equals("processTask")) {
                    ActivitiProcessTask parentTask = activitiProcessTaskService.getById(processTask.getPid());
                    if (parentTask.getType().equals("ALLOCATION")) {

                        /**
                         * 如果来源是调拨单
                         * 对应生成入库单
                         */
                        List<ProductionPickListsCart> pickListsCarts = pickListsCartService.query().eq("pick_lists_id", processTask.getFormId()).eq("status", 99).list();
                        Allocation allocation = allocationService.getById(parentTask.getFormId());
                        List<AllocationCart> allocationCarts = allocationCartService.query().eq("allocation_id", allocation.getAllocationId()).eq("pick_lists_id", pickLists.getPickListsId()).eq("status", 98).list();

                        List<InstockListParam> instockListParams = BeanUtil.copyToList(pickListsCarts, InstockListParam.class);
                        for (InstockListParam instockListParam : instockListParams) {
                            instockListParam.setStatus(0L);
                        }
                        List<InstockListParam> totalList = new ArrayList<>();
                        instockListParams.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
                                (id, transfer) -> {
                                    transfer.stream().reduce((a, b) -> new InstockListParam() {{
                                        setSkuId(a.getSkuId());
                                        setNumber(a.getNumber() + b.getNumber());
                                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                                    }}).ifPresent(totalList::add);
                                }
                        );
                        for (InstockListParam instockListParam : totalList) {
                            List<Long> inkindIds = new ArrayList<>();
                            for (InstockListParam listParam : instockListParams) {
                                if (instockListParam.getSkuId().equals(listParam.getSkuId()) && instockListParam.getBrandId().equals(listParam.getBrandId())) {
                                    inkindIds.add(listParam.getInkindId());
                                }
                            }
                            instockListParam.setInkindIds(inkindIds);
                        }
                        InstockOrderParam instockOrderParam = new InstockOrderParam();

                        instockOrderParam.setUserId(allocation.getUserId());

                        switch (allocation.getAllocationType()) {
                            case 1:
                                instockOrderParam.setStoreHouseId(allocation.getStorehouseId());
                                break;
                            case 2:
                                instockOrderParam.setStoreHouseId(allocationCarts.get(0).getStorehouseId());
                                break;
                        }
                        instockOrderParam.setType("调拨入库");
                        instockOrderParam.setSource("processTask");
                        if (ToolUtil.isNotEmpty(processTask.getMainTaskId())) {
                            instockOrderParam.setMainTaskId(processTask.getMainTaskId());
                        }
                        instockOrderParam.setSourceId(parentTask.getProcessTaskId());
                        instockOrderParam.setPid(parentTask.getProcessTaskId());
                        instockOrderParam.setListParams(totalList);
                        InstockOrder addEntity = instockOrderService.add(instockOrderParam);
                        for (AllocationCart allocationCart : allocationCarts) {
                            allocationCart.setInstockOrderId(addEntity.getInstockOrderId());
                        }
                        allocationCartService.updateBatchById(allocationCarts);
                    }
                    break;
                }

        }
    }


    private void updateRefuseStatus(ActivitiProcessTask processTask) {
        switch (processTask.getType()) {
            case "purchasePlan":
                procurementPlanService.updateRefuseStatus(processTask);
                break;
            case "inQuality":
            case "outQuality":
                break;
            case "purchaseAsk":
                purchaseAskService.updateRefuseStatus(processTask);
                break;
            case "procurementOrder":
                procurementOrderService.updateRefuseStatus(processTask);
                break;
            case "inQuiry":
            case "inquiry":
                inquiryTaskService.updateRefuseStatus(processTask);
                break;
            case "Error":
                instockOrderService.updateRefuseStatus(processTask);
                break;
            case "INSTOCK":
                instockOrderService.updateCreateInstockRefuseStatus(processTask);
                break;
            case "OUTSTOCK":
                pickListsService.updateOutStockRefuseStatus(processTask);
                break;

        }
    }

    private void refuseTask(ActivitiProcessTask processTask) {
        processTask.setStatus(50);
        activitiProcessTaskService.updateById(processTask);
        this.updateRefuseStatus(processTask);
        taskSend.refuseTask(processTask.getProcessTaskId());
    }

    private void loopNext(ActivitiProcessTask task, List<ActivitiAudit> activitiAuditList, List<ActivitiSteps> allSteps, Boolean auditCheck, Long loginUserId) {

        List<ActivitiProcessLog> audit = this.getAudit1(task.getProcessTaskId());

        if (auditCheck) {
            this.sendNextStepsByTask(task, audit, loginUserId);
        }
        List<ActivitiProcessLog> logs = listByTaskId(task.getProcessTaskId());

        /**
         * 更新下一个审批节点之前的所有抄送
         */
        for (ActivitiProcessLog activitiProcessLog : audit) {
            /**
             * 取节点规则
             */
            ActivitiAudit activitiAudit = getRule(activitiAuditList, activitiProcessLog.getSetpsId());

            if (ToolUtil.isNotEmpty(activitiAudit) && activitiAudit.getType().equals("send")) {
                updateStatus(activitiProcessLog.getLogId(), task.getProcessTaskId(), 1, loginUserId);

                ActivitiSteps activitiSteps = getSteps(allSteps, activitiProcessLog.getSetpsId());
                List<ActivitiProcessLog> processLogs = updateSupper(allSteps, logs, activitiSteps,loginUserId);


                if (processLogs.size() > 0) {
                    for (ActivitiProcessLog processLog : processLogs) {
                        processLog.setStatus(1);
                    }
                    this.updateBatchById(processLogs);
                    loopNext(task, activitiAuditList, allSteps, auditCheck, loginUserId);
                }
            }
        }
    }

    private void updateStatus(Long logId, Long taskId, Integer status, Long loginUserId) {
        ActivitiProcessLog entity = new ActivitiProcessLog();
        entity.setStatus(status);
        entity.setLogId(logId);
        entity.setUpdateUser(loginUserId);
        this.updateById(entity);

        /**
         * 添加动态
         */
         String content = "";
        switch (status) {
            case 1:
                content = "同意了申请";
                break;
        }

        shopCartService.addDynamicByTaskId(taskId, null, content);

    }


    /**
     * 检查节点动作完成(上)
     *
     * @param
     * @param
     * @param-
     */
    @Override
    public void checkAction(Long id, String formType, Long actionId, Long loginUserId) {

        ActivitiProcessTask processTask = activitiProcessTaskService.query().eq("type", formType).eq("form_id", id).eq("display", 1).one();
        if (ToolUtil.isEmpty(processTask)) {
            throw new ServiceException(500, "当前任务不存在");
        }
        List<ActivitiProcessLog> logs = this.getAudit3(processTask.getProcessTaskId());
        logs.addAll(this.getAudit1(processTask.getProcessTaskId()));
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : logs) {
            stepIds.add(processLog.getSetpsId());
        }
        List<ActivitiSteps> activitiSteps = stepIds.size() == 0 ? new ArrayList<>() : stepsService.listByIds(stepIds);
        for (ActivitiProcessLog processLog : logs) {
            for (ActivitiSteps activitiStep : activitiSteps) {
                if (processLog.getSetpsId().equals(activitiStep.getSetpsId()) && activitiStep.getStepType().equals("status")) {
                    this.checkLogActionComplete(processTask.getProcessTaskId(), activitiStep.getSetpsId(), actionId, loginUserId);
                    return;
                }
            }
        }
    }

    public void checkAction(Long id, Long actionId, Long loginUserId) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(id);
        List<ActivitiProcessLog> logs = this.getAudit3(processTask.getProcessTaskId());
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : logs) {
            stepIds.add(processLog.getSetpsId());
        }
        List<ActivitiSteps> activitiSteps = stepIds.size() == 0 ? new ArrayList<>() : stepsService.listByIds(stepIds);
        for (ActivitiProcessLog processLog : logs) {
            for (ActivitiSteps activitiStep : activitiSteps) {
                if (processLog.getSetpsId().equals(activitiStep.getSetpsId()) && activitiStep.getStepType().equals("status")) {
                    this.checkLogActionComplete(processTask.getProcessTaskId(), activitiStep.getSetpsId(), actionId, loginUserId);
                }
            }
        }
    }

    /**
     * 检查节点动作完成(下)
     *
     * @param taskId
     * @param stepId
     * @param
     */
    @Override
    public void checkLogActionComplete(Long taskId, Long stepId, Long actionId, Long loginUserId) {
        List<ActivitiProcessLog> processLogs = this.query().eq("task_id", taskId).eq("setps_id", stepId).list();
        ActivitiAuditResult audit = auditService.getAudit(stepId);

        //TODO 等待动作节点更换逻辑后 增加人员判断
//        if (this.checkUser(audit.getRule(), taskId)) {
        for (ActivitiProcessLog processLog : processLogs) {
            if ((ToolUtil.isEmpty(processLog.getAuditUserId()) || ((ToolUtil.isNotEmpty(processLog.getAuditUserId()) && processLog.getAuditUserId().equals(loginUserId)))) && ToolUtil.isNotEmpty(processLog.getActionStatus())) {
                List<ActionStatus> actionStatuses = JSON.parseArray(processLog.getActionStatus(), ActionStatus.class);
                for (ActionStatus actionStatus : actionStatuses) {
                    if (actionStatus.getActionId().equals(actionId)) {
                        actionStatus.setStatus(1);
                    }
                }
                processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                this.updateById(processLog);

                boolean isChecked = false;
                boolean completeFlag = true;



                actionStatuses.removeIf(i->!i.getChecked());
                if (actionStatuses.stream().allMatch(i->i.getStatus().equals(1) && i.getChecked())) {
                    this.autoAudit(taskId, 1, loginUserId);

                }
//                for (ActionStatus actionStatus : actionStatuses) {
//                    if (actionStatus.getStatus().equals(0) && actionStatus.getChecked()) {
//                        completeFlag = false;
//
//                        break;
//                    } else if (actionStatus.getStatus().equals(0)) {
//                        completeFlag = false;
//                        break;
//                    }
//                    if (actionStatus.getChecked()) {
//                        isChecked = true;
//                    }
//                }
//                if (completeFlag && isChecked) {
//                }
            }
        }

//        }
    }

    /**
     * 确认质检任务状态 （未完成 0）/（完成 1）/（待入库 2）
     *
     * @param
     * @return
     */


    private List<ActivitiProcessLog> updateSupper
    (List<ActivitiSteps> steps, List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps,Long loginUser) {
        List<ActivitiProcessLog> logs = new ArrayList<>();

        for (ActivitiSteps step : steps) {
            // 取当前步骤上级
            if (step.getSetpsId().equals(activitiSteps.getSetpsId())) {
                ActivitiProcessLog log = getLog(processLogs, activitiSteps);
                switch (step.getType()) {
                    case AUDIT:
                    case SEND:
                        //TODO 原判断 if (log.getStatus().equals(1))
                        if (log.getStatus().equals(1)) {
                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId()) && ((ToolUtil.isNotEmpty(processLog.getAuditUserId()) && processLog.getAuditUserId().equals(loginUser) )||ToolUtil.isEmpty(processLog.getAuditUserId())) ) {
                                    processLog.setStatus(1);
                                }
                            }
                            if (log.getStatus().equals(-1) || log.getStatus().equals(3)) {
                                logs.add(log);
                            }
//                            logs.add(log);
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()),loginUser);
                            logs.addAll(newLogs);
                        }
                        break;
                    case BRANCH:
                        // 添加当前步骤log
                        if (judgeNext(processLogs, steps, activitiSteps)) {

                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId())) {
                                    processLog.setStatus(1);
                                }
                            }
                            logs.add(log);
                            // 下级完成递归传入当前步骤判断上一级
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()),loginUser);
                            logs.addAll(newLogs);
                        }
                        break;

                    case ROUTE:

                        boolean state = true;
                        String[] split = step.getConditionNodes().split(",");

                        for (String branchSteps : split) {
                            for (ActivitiProcessLog activitiProcessLog : processLogs) {
                                if (activitiProcessLog.getSetpsId().toString().equals(branchSteps)) {
                                    if (activitiProcessLog.getStatus() != 1 && activitiProcessLog.getStatus() != 2) {
                                        state = false;
                                        break;
                                    }
                                }
                            }
                            if (!state) {
                                break;
                            }
                        }

                        if (state) {
                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId())) {
                                    processLog.setStatus(1);
                                }
                            }
                            logs.add(log);
                            List<ActivitiProcessLog> routeLog = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()),loginUser);
                            logs.addAll(routeLog);
                        }
//                        return logs;
                        break;
                }
                return logs;
            }
        }

        return logs;

    }

    /**
     * 取上级log
     *
     * @param processLogs
     * @param activitiSteps
     * @return
     */
    private ActivitiProcessLog getLastLog(List<ActivitiProcessLog> processLogs, ActivitiSteps
            activitiSteps) {

        for (ActivitiProcessLog processLog : processLogs) {
            // 取当前步骤log
            if (processLog.getSetpsId().equals(activitiSteps.getSupper())) {
                return processLog;

            }
        }
        return new ActivitiProcessLog();
    }


    /**
     * 取当前log
     *
     * @param processLogs
     * @param activitiSteps
     * @return
     */
    private ActivitiProcessLog getLog(List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps) {

        for (ActivitiProcessLog processLog : processLogs) {
            // 取当前步骤log
            if (processLog.getSetpsId().equals(activitiSteps.getSetpsId())) {
                return processLog;

            }
        }
        return null;
    }

    /**
     * @return
     */
    private Boolean judgeNext(List<ActivitiProcessLog> processLogs, List<ActivitiSteps> steps, ActivitiSteps
            activitiSteps) {

        if (ToolUtil.isEmpty(activitiSteps.getChildren())) {
            return true;
        }
        ActivitiSteps activityStepsChild = getchildStep(steps, activitiSteps);

        if (ToolUtil.isEmpty(activityStepsChild)) {
            return true;
        }

        ActivitiProcessLog processLog = getLog(processLogs, activityStepsChild);

        if (ToolUtil.isNotEmpty(processLog)) {
            switch (activityStepsChild.getType()) {
                case AUDIT:
                case SEND:
                case ROUTE:
                    return processLog.getStatus() == 1;
                default:
                    return true;
            }

        }

        return true;
    }


    private ActivitiSteps getSteps(List<ActivitiSteps> steps, Long stepId) {
        for (ActivitiSteps step : steps) {
            if (step.getSetpsId().toString().equals(stepId.toString())) {
                return step;
            }
        }
        return null;
    }

    ;

    /**
     * 取下一级
     *
     * @param steps
     * @param activitiSteps
     * @return
     */
    private ActivitiSteps getchildStep(List<ActivitiSteps> steps, ActivitiSteps activitiSteps) {
        for (ActivitiSteps step : steps) {
            if (activitiSteps.getChildren().equals(step.getSetpsId().toString())) {
                return step;
            }
        }
        return null;
    }

    @Override
    public Boolean checkUser(AuditRule starUser, Long taskId) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Long userId = user.getId();
        List<Long> users = taskSend.selectUsers(starUser, taskId);
        for (Long id : users) {
            if (ToolUtil.isNotEmpty(id) && id.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkUser(AuditRule starUser, Long loginUserId, Long taskId) {
        List<Long> users = taskSend.selectUsers(starUser, taskId);
        for (Long aLong : users) {
            if (aLong.equals(loginUserId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(ActivitiProcessLogParam param) {
        int admin = activitiProcessTaskService.isAdmin(param.getTaskId());
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        } else {
            param.setDisplay(0);
            ActivitiProcessLog entity = getEntity(param);
            this.updateById(entity);
        }

    }

    @Override
    public void update(ActivitiProcessLogParam param) {
        ActivitiProcessLog newEntity = getEntity(param);
        int admin = activitiProcessTaskService.isAdmin(newEntity.getTaskId());
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        } else {
            this.updateById(newEntity);
        }
    }


    /**
     * 取出当前待审核的所有节点
     *
     * @param
     * @return
     */
    @Override
    public List<ActivitiProcessLog> getAudit3(Long taskId) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            return new ArrayList<>();
        }
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(task.getProcessId());
        List<ActivitiProcessLog> activitiProcessLogs = listByTaskId(taskId);
        return loopAudit(activitiStepsResult, activitiProcessLogs);
    }

    private List<ActivitiProcessLog> loopAudit(ActivitiStepsResult
                                                       activitiStepsResult, List<ActivitiProcessLog> activityProcessLog) {
        List<ActivitiProcessLog> activitiStepsResultList = new ArrayList<>();

        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return activitiStepsResultList;
        }

        List<ActivitiProcessLog> logs = getLog(activitiStepsResult.getSetpsId(), activityProcessLog);
        for (ActivitiProcessLog log : logs) {
            switch (activitiStepsResult.getType()) {
                case START:
                case AUDIT:

                    if (ToolUtil.isNotEmpty(log)) {
                        if (log.getStatus().equals(1) || log.getStatus().equals(4)) {
                            activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
                            //TODO 需要测试验证
                        } else if (log.getStatus().equals(3)) {
                            activitiStepsResultList.add(log);
                        }
                    }
                    break;
                case SEND:
                    if (ToolUtil.isNotEmpty(log)) {
                        if (!log.getStatus().equals(1)) {
                            activitiStepsResultList.add(log);
                        }
                        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                            activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
                        }
                    }
                    break;
                case BRANCH:
                    if (ToolUtil.isNotEmpty(log)) {
                        if (!log.getStatus().equals(2)) {
                            activitiStepsResultList.add(log);
                            if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                                activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
                            }
                        }
                    }
                    break;
                case ROUTE:
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
                        if (ToolUtil.isNotEmpty(log) && log.getStatus().equals(1)) {
                            activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
                        } else {
                            activitiStepsResultList.add(log);
                            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                                activitiStepsResultList.addAll(loopAudit(stepsResult, activityProcessLog));
                            }
                        }

                    }
                    break;
            }
        }
        return activitiStepsResultList;
    }

    /**
     * 取出当前待审核的所有节点
     *
     * @param
     * @return
     */
    @Override
    public List<ActivitiProcessLog> getAudit1(Long taskId) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            return new ArrayList<>();
        }
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(task.getProcessId());
        List<ActivitiProcessLog> activitiProcessLogs = listByTaskId(taskId);
        return loopAudit1(activitiStepsResult, activitiProcessLogs);
    }

    private List<ActivitiProcessLog> loopAudit1(ActivitiStepsResult
                                                        activitiStepsResult, List<ActivitiProcessLog> activityProcessLog) {
        List<ActivitiProcessLog> activitiStepsResultList = new ArrayList<>();

        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return activitiStepsResultList;
        }

        List<ActivitiProcessLog> logs = getLog(activitiStepsResult.getSetpsId(), activityProcessLog);
        for (ActivitiProcessLog log : logs) {
            switch (activitiStepsResult.getType()) {
                case START:
                case AUDIT:

                    if (ToolUtil.isNotEmpty(log)) {
                        if (log.getStatus().equals(1) || log.getStatus().equals(4)) {
                            activitiStepsResultList.addAll(loopAudit1(activitiStepsResult.getChildNode(), activityProcessLog));
                        } else {
                            activitiStepsResultList.add(log);
                        }
                    }
                    break;
                case SEND:
                    if (ToolUtil.isNotEmpty(log)) {
                        if (!log.getStatus().equals(1)) {
                            activitiStepsResultList.add(log);
                        }
                        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                            activitiStepsResultList.addAll(loopAudit1(activitiStepsResult.getChildNode(), activityProcessLog));
                        }
                    }
                    break;
                case BRANCH:
                    if (ToolUtil.isNotEmpty(log)) {
                        if (!log.getStatus().equals(2)) {
                            activitiStepsResultList.add(log);
                            if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                                activitiStepsResultList.addAll(loopAudit1(activitiStepsResult.getChildNode(), activityProcessLog));
                            }
                        }
                    }
                    break;
                case ROUTE:
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
                        if (ToolUtil.isNotEmpty(log) && log.getStatus().equals(1)) {
                            activitiStepsResultList.addAll(loopAudit1(activitiStepsResult.getChildNode(), activityProcessLog));
                        } else {
                            activitiStepsResultList.add(log);
                            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                                activitiStepsResultList.addAll(loopAudit1(stepsResult, activityProcessLog));
                            }
                        }

                    }
                    break;
            }
        }
        return activitiStepsResultList;
    }

    private List<ActivitiProcessLog> getLog(Long setpsId, List<ActivitiProcessLog> activityProcessLog) {
        //TODO 更改新的log匹配 返回集合
        List<ActivitiProcessLog> result = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : activityProcessLog) {
            if (activitiProcessLog.getSetpsId().equals(setpsId)) {
                result.add(activitiProcessLog);
            }
        }
        return result;
    }

    @Override
    public ActivitiStepsResult addLog(Long processId, Long taskId) {
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);
        loopAdd(activitiStepsResult, taskId);
        viewService.addView(taskId);
        return activitiStepsResult;
    }

    @Override
    public ActivitiStepsResult addLog(Long processId, Long taskId, Integer status, Long loginUserId) {
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);
        loopAdd(activitiStepsResult, taskId, status, loginUserId);
        viewService.addView(taskId);
        return activitiStepsResult;
    }

    @Override
    public ActivitiStepsResult microAddLog(Long processId, Long taskId, Long userId) {
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);
        loopAdd(activitiStepsResult, taskId);
        viewService.microAddView(taskId, userId);
        return activitiStepsResult;
    }


    public void addLogJudgeBranch(Long processId, Long taskId, Long sourId, String type) {
        //TODO 分支添加log
        ActivitiStepsResult stepResult = stepsService.getStepResult(processId);
        switch (type) {
            case "purchaseAsk":
                PurchaseAsk purchaseAsk = askService.getById(sourId);
                loopAddJudgeBranch(stepResult, taskId, purchaseAsk);
                viewService.addView(taskId);
                break;

        }

    }

    private void loopAdd(ActivitiStepsResult activitiStepsResult, Long taskId, Integer status, Long loginUserId) {
        Long processId = activitiStepsResult.getProcessId();

        /**
         * insert
         */
        //TODO 根据节点规则 给每个人都创建上log

        List<ActivitiProcessLog> processLogs = new ArrayList<>();
        List<Long> users = taskSend.selectUsers(activitiStepsResult.getAuditRule(), taskId);
        if (ToolUtil.isNotEmpty(users)) {
            for (Long user : users) {
                ActivitiProcessLog processLog = new ActivitiProcessLog();
                processLog.setPeocessId(processId);
                processLog.setTaskId(taskId);
                processLog.setSetpsId(activitiStepsResult.getSetpsId());
                processLog.setStatus(status);
                processLog.setUpdateUser(loginUserId);
                processLog.setCreateUser(loginUserId);
                processLog.setUpdateTime(new Date());
                processLog.setAuditUserId(user);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
                    List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();
                    if (ToolUtil.isNotEmpty(actionStatuses)) {
                        for (ActionStatus actionStatus : actionStatuses) {
                            actionStatus.setStatus(0);
                        }
                        processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                    }
                }
                processLogs.add(processLog);
            }
            this.saveBatch(processLogs);
        } else {
            ActivitiProcessLog processLog = new ActivitiProcessLog();
            processLog.setPeocessId(processId);
            processLog.setTaskId(taskId);
            processLog.setSetpsId(activitiStepsResult.getSetpsId());
            processLog.setStatus(status);
            processLog.setUpdateUser(loginUserId);
            processLog.setCreateUser(loginUserId);
            processLog.setUpdateTime(new Date());
            if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
                List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();
                if (ToolUtil.isNotEmpty(actionStatuses)) {
                    for (ActionStatus actionStatus : actionStatuses) {
                        actionStatus.setStatus(0);
                    }
                    processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                }
            }
            this.save(processLog);
        }

        if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                loopAdd(stepsResult, taskId, status, loginUserId);
            }
        }
        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
            loopAdd(activitiStepsResult.getChildNode(), taskId, status, loginUserId);
        }

    }

    private void loopAdd(ActivitiStepsResult activitiStepsResult, Long taskId) {
        List<ActivitiProcessLog> processLogs = new ArrayList<>();

        Long processId = activitiStepsResult.getProcessId();
        List<Long> users = taskSend.selectUsers(activitiStepsResult.getAuditRule(), taskId);
        if (activitiStepsResult.getType().toString().equals(StepsType.START.getType())) {
            ActivitiProcessLog processLog = new ActivitiProcessLog();
            processLog.setPeocessId(processId);
            processLog.setTaskId(taskId);
            processLog.setSetpsId(activitiStepsResult.getSetpsId());
            processLog.setStatus(3);
            processLog.setAuditUserId(LoginContextHolder.getContext().getUserId());
            if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
                List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();
                if (ToolUtil.isNotEmpty(actionStatuses)) {
                    for (ActionStatus actionStatus : actionStatuses) {
                        actionStatus.setStatus(0);
                    }
                    processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                }
            }
            this.save(processLog);
        } else {
            if (users.size() > 0) {
                for (Long user : users) {
                    /**
                     * insert
                     */
                    ActivitiProcessLog processLog = new ActivitiProcessLog();
                    processLog.setPeocessId(processId);
                    processLog.setTaskId(taskId);
                    processLog.setSetpsId(activitiStepsResult.getSetpsId());
                    processLog.setStatus(-1);
                    processLog.setAuditUserId(user);
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
                        List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();
                        if (ToolUtil.isNotEmpty(actionStatuses)) {
                            for (ActionStatus actionStatus : actionStatuses) {
                                actionStatus.setStatus(0);
                            }
                            processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                        }
                    }
                    processLogs.add(processLog);
                }

                this.saveBatch(processLogs);
            } else {
                ActivitiProcessLog processLog = new ActivitiProcessLog();
                processLog.setPeocessId(processId);
                processLog.setTaskId(taskId);
                processLog.setSetpsId(activitiStepsResult.getSetpsId());
                processLog.setStatus(-1);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
                    List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();
                    if (ToolUtil.isNotEmpty(actionStatuses)) {
                        for (ActionStatus actionStatus : actionStatuses) {
                            actionStatus.setStatus(0);
                        }
                        processLog.setActionStatus(JSON.toJSONString(actionStatuses));
                    }
                }
                this.save(processLog);
            }
        }


        if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                loopAdd(stepsResult, taskId);
            }
        }
        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
            loopAdd(activitiStepsResult.getChildNode(), taskId);
        }

    }


    @Override
    public ActivitiProcessLogResult findBySpec(ActivitiProcessLogParam param) {
        return null;
    }

    @Override
    public List<ActivitiProcessLogResult> findListBySpec(ActivitiProcessLogParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(ActivitiProcessLogParam param) {
        Page<ActivitiProcessLogResult> pageContext = getPageContext();
        IPage<ActivitiProcessLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }


    private Serializable getKey(ActivitiProcessLogParam param) {
        return param.getLogId();
    }

    private Page<ActivitiProcessLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiProcessLog getOldEntity(ActivitiProcessLogParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcessLog getEntity(ActivitiProcessLogParam param) {
        ActivitiProcessLog entity = new ActivitiProcessLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    public void sendNextStepsByTask(ActivitiProcessTask task, List<ActivitiProcessLog> audit, Long loginUserId) {
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "未找到相关流程任务");
        }
//        List<ActivitiProcessLog> logs = this.listByTaskId(task.getProcessTaskId());
//        audit = this.getAudit(task.getProcessId(), logs);

        List<Long> nextStepsIds = new ArrayList<Long>() {{
            add(0L);
        }};

        for (ActivitiProcessLog activitiProcessLog : audit) {
            nextStepsIds.add(activitiProcessLog.getSetpsId());
        }

        if (ToolUtil.isNotEmpty(nextStepsIds)) {
            List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", nextStepsIds.stream().distinct().collect(Collectors.toList()));
            }});

            for (ActivitiAudit activitiAudit : activitiAudits) {

                if (ToolUtil.isNotEmpty(activitiAudit) && !activitiAudit.getType().equals("route") && !activitiAudit.getType().equals("branch")) {
                    RuleType ruleType = activitiAudit.getRule().getType();
                    taskSend.send(ruleType, activitiAudit.getRule(), task.getProcessTaskId(), loginUserId);
                }
            }
        }
    }


    @Override
    public Boolean judgeStatus(ActivitiProcessTask task, RuleType ruleType) {

        //取出当前所有规则
        List<ActivitiSteps> activitiSteps = stepsService.list(new QueryWrapper<ActivitiSteps>() {{
            eq("process_id", task.getProcessId());
        }});
        List<Long> setpIds = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            setpIds.add(activitiStep.getSetpsId());
        }
        List<ActivitiAudit> activitiAudits = auditService.getListByStepsId(setpIds);

        //过滤路由和条件
        List<ActivitiAudit> activitiAuditList = new ArrayList<>();
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (ToolUtil.isNotEmpty(activitiAudit.getRule()) && activitiAudit.getRule().getType().equals(ruleType)) {
                activitiAuditList.add(activitiAudit);
            }
        }
        //取出当前任务所有log
        List<ActivitiProcessLog> processLogs = this.query().eq("task_id", task.getProcessTaskId()).list();
        for (ActivitiAudit activitiAudit : activitiAuditList) {
            return judegSupper(activitiAudit.getSetpsId(), activitiSteps, processLogs);
        }
        return false;
    }

    @Override
    public List<ActivitiProcessLogResult> getLogByTaskProcess(Long processId, Long taskId) {
        List<ActivitiProcessLogResult> logResults = new ArrayList<>();

        List<ActivitiProcessLog> processLogs = this.list(new QueryWrapper<ActivitiProcessLog>() {{
            eq("peocess_id", processId);
            eq("task_id", taskId);
        }});
        for (ActivitiProcessLog processLog : processLogs) {
            ActivitiProcessLogResult logResult = new ActivitiProcessLogResult();
            ToolUtil.copyProperties(processLog, logResult);
            logResults.add(logResult);
        }
        this.logActionFormat(logResults);
        return logResults;
    }

    /**
     * 格式化log动作
     *
     * @param param
     */
    private void logActionFormat(List<ActivitiProcessLogResult> param) {
        List<Long> actionIds = new ArrayList<>();
        List<Long> auditUserIds = new ArrayList<>();
        for (ActivitiProcessLogResult activitiProcessLogResult : param) {
            if (ToolUtil.isNotEmpty(activitiProcessLogResult.getActionStatus())) {
                List<ActionStatus> actionStatuses = JSON.parseArray(activitiProcessLogResult.getActionStatus(), ActionStatus.class);
                for (ActionStatus actionStatus : actionStatuses) {
                    actionIds.add(actionStatus.getActionId());
                }
            }
            if (ToolUtil.isNotEmpty(activitiProcessLogResult.getAuditUserId())) {
                auditUserIds.add(activitiProcessLogResult.getAuditUserId());
            }
        }
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(auditUserIds);


        List<DocumentsAction> documentsActions = actionIds.size() == 0 ? new ArrayList<>() : documentsActionService.listByIds(actionIds);
        List<DocumentsActionResult> results = new ArrayList<>();
        for (DocumentsAction documentsAction : documentsActions) {
            DocumentsActionResult result = new DocumentsActionResult();
            ToolUtil.copyProperties(documentsAction, result);
            results.add(result);
        }
        for (ActivitiProcessLogResult activitiProcessLogResult : param) {
            if (ToolUtil.isNotEmpty(activitiProcessLogResult.getActionStatus())) {
                List<DocumentsActionResult> documentsActionResults = new ArrayList<>();
                List<ActionStatus> actionStatuses = JSON.parseArray(activitiProcessLogResult.getActionStatus(), ActionStatus.class);
                for (ActionStatus actionStatus : actionStatuses) {
                    for (DocumentsActionResult result : results) {
                        if (actionStatus.getActionId().equals(result.getDocumentsActionId())) {
                            result.setStatus(actionStatus.getStatus());
                            documentsActionResults.add(result);
                        }
                    }
                }
                activitiProcessLogResult.setActionResults(documentsActionResults);
            }
            for (UserResult userResultsById : userResultsByIds) {
                if (ToolUtil.isNotEmpty(activitiProcessLogResult.getAuditUserId()) && activitiProcessLogResult.getAuditUserId().equals(userResultsById.getUserId())) {
                    activitiProcessLogResult.setAuditUserResult(userResultsById);
                }
            }
        }
    }


    private Boolean judegSupper(Long
                                        setpsId, List<ActivitiSteps> stepsResults, List<ActivitiProcessLog> processLogs) {
        for (ActivitiSteps stepsResult : stepsResults) {
            if (setpsId.equals(stepsResult.getSetpsId())) {
                if (stepsResult.getType().toString().equals("4") || stepsResult.getType().toString().equals("3")) {
                    return judegSupper(stepsResult.getSupper(), stepsResults, processLogs);
                } else {
                    for (ActivitiProcessLog processLog : processLogs) {
                        if (stepsResult.getSupper().equals(processLog.getSetpsId())) {
                            return processLog.getStatus().equals(1);
                        }
                    }
                }
            }
        }
        return false;
    }


    @Override
    public List<ActivitiProcessLogResult> getLogAudit(Long taskId) {
        List<ActivitiProcessLog> processLogs = this.query().eq("task_id", taskId).list();

        List<Long> stepIds = new ArrayList<>();
        List<ActivitiProcessLogResult> logResults = new ArrayList<>();

        for (ActivitiProcessLog processLog : processLogs) {
            stepIds.add(processLog.getSetpsId());
            ActivitiProcessLogResult logResult = new ActivitiProcessLogResult();
            ToolUtil.copyProperties(processLog, logResult);
            logResults.add(logResult);
        }
        List<ActivitiAudit> audits = stepIds.size() == 0 ? new ArrayList<>() : auditService.query().in("setps_id", stepIds).list();

        for (ActivitiProcessLogResult logResult : logResults) {
            for (ActivitiAudit audit : audits) {
                if (logResult.getSetpsId().equals(audit.getSetpsId())) {
                    logResult.setActivitiAudit(audit);
                    break;
                }
            }
        }
        return logResults;
    }

    private void loopAddJudgeBranch(ActivitiStepsResult activitiStepsResult, Long taskId, PurchaseAsk purchaseAsk) {
        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return;
        }
        Long processId = activitiStepsResult.getProcessId();

        /**
         * insert
         */
        ActivitiProcessLog processLog = new ActivitiProcessLog();
        if (ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule()) && ToolUtil.isNotEmpty(activitiStepsResult.getAuditRule().getActionStatuses())) {
            List<ActionStatus> actionStatuses = activitiStepsResult.getAuditRule().getActionStatuses();   //添加log 动作状态
            if (ToolUtil.isNotEmpty(actionStatuses)) {
                for (ActionStatus actionStatus : actionStatuses) {
                    actionStatus.setStatus(0);
                }
                processLog.setActionStatus(JSON.toJSONString(actionStatuses));
            }
        }

        processLog.setPeocessId(processId);
        processLog.setTaskId(taskId);
        processLog.setSetpsId(activitiStepsResult.getSetpsId());
        processLog.setStatus(-1);
        //判断分支条件
        if (activitiStepsResult.getType().toString().equals("3")) {
            AuditRule auditRule = activitiStepsResult.getAuditRule();
            if (ToolUtil.isEmpty(auditRule)) {
                throw new ServiceException(500, "请先在分支上设置条件");
            }
            boolean b = true;
            for (AuditRule.Rule rule : auditRule.getRules()) {
                AuditRule.PurchaseAsk ask = rule.getPurchaseAsk();
                switch (rule.getType()) {
                    case type_number: //类型数量
                        if (!judeg(ask, purchaseAsk.getTypeNumber())) {
                            b = false;
                        }
                        break;
                    case number: //总共数量
                        if (!judeg(ask, purchaseAsk.getNumber())) {
                            b = false;
                        }
                        break;
                }
            }

            if (!b) {
                processLog.setStatus(2);
            }
        }
        this.save(processLog);


        if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                loopAddJudgeBranch(stepsResult, taskId, purchaseAsk);
            }
        }
        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
            loopAddJudgeBranch(activitiStepsResult.getChildNode(), taskId, purchaseAsk);
        }

    }

    /**
     * 比对
     *
     * @param ask
     * @param number
     * @return
     */
    private Boolean judeg(AuditRule.PurchaseAsk ask, Long number) {
        switch (ask.getOperator()) {
            case ">":
                return number > ask.getValue();
            case ">=":
                return number >= ask.getValue();
            case "===":
                return number.toString().equals(ask.getValue().toString());
            case "<":
                return number < ask.getValue();
            case "<=":
                return number <= ask.getValue();
            case "!=":
                return !number.toString().equals(ask.getValue().toString());
        }
        return false;
    }

    @Override
    public List<ActivitiProcessLogResult> auditList(ActivitiProcessLogParam param) {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<Long> stepIds = getStepIdsByType("audit", userId);
        List<ActivitiProcessLogResult> logResults = this.baseMapper.auditList(stepIds, param);
        format(logResults);
        return logResults;
    }


    @Override
    public List<ActivitiProcessLogResult> sendList(ActivitiProcessLogParam param) {
        List<Long> ids = getStepIdsByType("send");
        List<ActivitiProcessLogResult> logResults = this.baseMapper.sendList(ids, param);
        format(logResults);
        return logResults;
    }

    /**
     * 查出当前下的所有步骤
     *
     * @param type
     * @return
     */
    List<Long> getStepIdsByType(String type) {
        List<ActivitiAudit> audits = auditService.list();
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiAudit audit : audits) {
            AuditRule rule = audit.getRule();
            if (ToolUtil.isNotEmpty(rule)) {
                if (ToolUtil.isNotEmpty(rule.getType()) && rule.getType().toString().equals(type)) {
                    stepIds.add(audit.getSetpsId());
                }
            }
        }
        return stepIds;
    }

    /**
     * 是否邮操作权限
     *
     * @param type
     * @param module
     * @param action
     * @return
     */

    @Override
    public boolean canOperat(String type, String module, String action) {

        ActivitiProcess process = processService.query().eq("type", type).eq("module", module).eq("status", 99).one();
        if (ToolUtil.isEmpty(process)) {
            throw new ServiceException(500, "没有流程");
        }
        List<ActivitiSteps> steps = stepsService.query().eq("process_id", process.getProcessId()).list();
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiSteps step : steps) {
            stepIds.add(step.getSetpsId());
        }

        List<ActivitiAudit> audits = auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepIds);
        }});

        for (ActivitiAudit audit : audits) {
            AuditRule rule = audit.getRule();
            if (ToolUtil.isNotEmpty(rule) && ToolUtil.isNotEmpty(rule.getActionStatuses())) {
                for (ActionStatus actionStatus : rule.getActionStatuses()) {
                    if (actionStatus.getAction().equals(action)) {
                        return activitiProcessTaskService.startHaveME(rule, LoginContextHolder.getContext());
                    }
                }
            }
        }

        return false;
    }


    /**
     * 查出当前下关于用户所有步骤
     *
     * @param type
     * @return
     */
    List<Long> getStepIdsByType(String type, Long userId) {
        List<ActivitiAudit> audits = auditService.list();
        List<Long> stepIds = new ArrayList<>();

        for (ActivitiAudit audit : audits) {
            AuditRule rule = audit.getRule();
            if (ToolUtil.isNotEmpty(rule)) {
                if (ToolUtil.isNotEmpty(rule.getType()) && rule.getType().toString().equals(type)) {
                    if (haveME(rule, userId)) {
                        stepIds.add(audit.getSetpsId());
                    }
                }
            }
        }

        return stepIds;
    }

    /**
     * 当前规则指定人
     *
     * @param rule
     * @param userId
     * @return
     */
    private boolean haveME(AuditRule rule, Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            return false;
        }
        for (AuditRule.Rule ruleRule : rule.getRules()) {
            for (AppointUser appointUser : ruleRule.getAppointUsers()) {
                if (appointUser.getKey().equals(userId.toString())) {
                    return true;
                }
            }
        }
        return false;
    }


    void format(List<ActivitiProcessLogResult> data) {
        List<Long> taskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ActivitiProcessLogResult datum : data) {
            taskIds.add(datum.getTaskId());
            userIds.add(datum.getCreateUser());
        }
        List<ActivitiProcessTask> tasks = taskIds.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.listByIds(taskIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);


        for (ActivitiProcessLogResult datum : data) {

            for (ActivitiProcessTask task : tasks) {

                if (datum.getTaskId().equals(task.getProcessTaskId())) {
                    ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
                    ToolUtil.copyProperties(task, taskResult);
                    datum.setTaskResult(taskResult);
                    break;
                }

            }

            for (User user : userList) {

                if (datum.getCreateUser().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }

            }
        }
    }

    @Override
    public void judgeLog(Long taskId, List<Long> logIds) {

        if (ToolUtil.isEmpty(taskId) || ToolUtil.isEmpty(logIds)) {
            throw new ServiceException(500, "缺少 taskId或logId");
        }

        List<ActivitiProcessLog> processLogs = this.query().eq("task_id", taskId).list();
        for (Long logId : logIds) {
            judgeStatus(logId, processLogs);
        }


    }

    private void judgeStatus(Long logId, List<ActivitiProcessLog> logs) {
        for (ActivitiProcessLog activitiProcessLog : logs) {
            if (activitiProcessLog.getLogId().equals(logId) && ( activitiProcessLog.getStatus() != 3 && activitiProcessLog.getStatus() != -1 )) {
                throw new ServiceException(500, "当前节点已被操作,请刷新页面");
            }
        }
    }

    @Override
    public List<ActivitiProcessLog> getAuditByForm(Long formId, String type) {
        ActivitiProcessTask processTask = activitiProcessTaskService.query().eq("type", type).eq("form_id", formId).eq("display", 1).one();
        List<ActivitiProcessLog> audit = this.getAudit3(processTask.getProcessTaskId());
        return audit;
    }

//    private void startAction(List<ActivitiProcessLog> audit, ActivitiProcessTask task) {
//        switch (task.getType()) {
//            case "ALLOCATION":
//                for (ActivitiProcessLog processLog : audit) {
//                    if (ToolUtil.isNotEmpty(processLog.getActionStatus())) {
//                        List<ActionStatus> actionStatuses = JSON.parseArray(processLog.getActionStatus(), ActionStatus.class);
//                        DocumentsAction action = documentsActionService.query().eq("action", AllocationActionEnum.carryAllocation.name()).eq("display", 1).one();
//                        for (ActionStatus actionStatus : actionStatuses) {
//                            if (actionStatus.getActionId().equals(action.getDocumentsActionId()) && actionStatus.getStatus().equals(0)) {
//                                allocationService.createPickListsAndInStockOrder(task.getFormId());
//                                continue;
//                            }
//                        }
//
//                    }
//                }
//                break;
//        }
//    }

}

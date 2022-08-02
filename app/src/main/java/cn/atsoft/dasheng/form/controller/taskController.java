package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
import cn.atsoft.dasheng.erp.service.impl.AllocationServiceImpl;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditParam;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/audit")
@Api(tags = "流程主表")
public class taskController extends BaseController {

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ActivitiProcessTaskService taskService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivitiProcessLogService logService;

    @Autowired
    private PurchaseAskService askService;

    @Autowired
    private ProductionPickListsService pickListsService;

    @Autowired
    private RemarksService remarksService;
    @Autowired
    private ProcurementOrderService procurementOrderService;
    @Autowired
    private ProcurementPlanService procurementPlanService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private MaintenanceService maintenanceService;
    @Autowired
    private StepsService appStepService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AllocationService allocationService;


    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData audit(@RequestBody AuditParam auditParam) {
        //添加备注
        remarksService.addNote(auditParam);
        this.activitiProcessLogService.judgeLog(auditParam.getTaskId(), auditParam.getLogIds());  //判断当前log状态
        this.activitiProcessLogService.audit(auditParam.getTaskId(), auditParam.getStatus());

        return ResponseData.success();
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ApiOperation("新建评论")
    public ResponseData addComments(@RequestBody AuditParam auditParam) {
        if (ToolUtil.isEmpty(auditParam.getTaskId())) {
            throw new ServiceException(500, "请检查任务id");
        }
        remarksService.addComments(auditParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData<ActivitiProcessTaskResult> detail(@Param("taskId") Long taskId) {
        //流程任务
        if (ToolUtil.isEmpty(taskId)) {
            throw new ServiceException(500, "缺少taskId");
        }
        ActivitiProcessTask processTask = taskService.getById(taskId);
        ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
        ToolUtil.copyProperties(processTask, taskResult);

        try {
            switch (taskResult.getType()) {
                case "quality_task":
                    QualityTaskResult task = qualityTaskService.getTask(taskResult.getFormId());
                    taskResult.setObject(task);
                    break;
                case "purchase":
                case "purchaseAsk":
                    PurchaseAskParam param = new PurchaseAskParam();
                    param.setPurchaseAskId(taskResult.getFormId());
                    PurchaseAskResult askResult = askService.detail(param);
                    taskResult.setObject(askResult);
                    break;
                case "procurementOrder":
                    ProcurementOrder detail = this.procurementOrderService.getById(taskResult.getFormId());
                    ProcurementOrderResult result = new ProcurementOrderResult();
                    ToolUtil.copyProperties(detail, result);
                    User user1 = userService.getById(result.getCreateUser());
                    result.setUser(user1);
                    taskResult.setObject(result);

                    break;
                case "purchasePlan":
                    ProcurementPlan procurementPlan = this.procurementPlanService.getById(taskResult.getFormId());
                    if (ToolUtil.isEmpty(procurementPlan)) {
                        return null;
                    }
                    ProcurementPlanResult procurementPlanResult = new ProcurementPlanResult();
                    ToolUtil.copyProperties(procurementPlan, procurementPlanResult);
                    User user = userService.getById(procurementPlanResult.getCreateUser());
                    procurementPlanResult.setFounder(user);
                    procurementPlanService.detail(procurementPlanResult);
                    taskResult.setObject(procurementPlanResult);
                    break;
                case "INSTOCK":
                    taskService.format(new ArrayList<ActivitiProcessTaskResult>() {{
                        add(taskResult);
                    }});
                    break;
                case "ERROR":
                    AnomalyOrderResult orderResult = anomalyOrderService.detail(taskResult.getFormId());
                    taskResult.setReceipts(orderResult);
                    break;
                case "ErrorForWard":
                    AnomalyResult anomalyResult = anomalyService.detail(taskResult.getFormId());
                    taskResult.setReceipts(anomalyResult);
                    break;
                case "OUTSTOCK":
                    ProductionPickListsResult pickListsRestult = pickListsService.detail(taskResult.getFormId());

                    taskResult.setReceipts(pickListsRestult);
//                    taskService.format(new ArrayList<ActivitiProcessTaskResult>(){{
//                        add(taskResult);
//                    }});
                    break;
                case "MAINTENANCE":
                    MaintenanceResult maintenanceResult = maintenanceService.detail(taskResult.getFormId());
                    taskResult.setReceipts(maintenanceResult);
                    break;
                case "Stocktaking":
                    InventoryResult inventoryResult = inventoryService.detail(taskResult.getFormId());
                    taskResult.setReceipts(inventoryResult);
                    break;
                case "ALLOCATION":
                    AllocationResult allocationResult = allocationService.detail(taskResult.getFormId());
                    taskResult.setReceipts(allocationResult);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //树形结构
        ActivitiStepsResult stepResult = stepsService.getStepResult(taskResult.getProcessId());
        //获取当前processTask 下的所有log
        List<ActivitiProcessLogResult> process = logService.getLogByTaskProcess(processTask.getProcessId(), taskId);
        //比对log
        stepsService.getStepLog(stepResult, process);
        //返回头像
        appStepService.headPortrait(stepResult);

        //取出所有未审核节点
        List<ActivitiProcessLog> audit = activitiProcessLogService.getAudit(taskId);

        /**
         * 流程中审核节点
         */
        List<Long> stepsIds = new ArrayList<>();
        for (ActivitiProcessLogResult processLog : process) {
            stepsIds.add(processLog.getSetpsId());
        }
        /**
         * 取出所有步骤配置
         */
        if (ToolUtil.isNotEmpty(stepsIds)) {
            List<ActivitiAudit> activitiAudits = auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", stepsIds);
            }});

            taskResult.setPermissions(false);
            for (ActivitiProcessLog activitiProcessLog : audit) {
                if (activitiProcessLog.getStatus() == -1) {
                    /**
                     * 取节点规则
                     */
                    ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());
                    if (ToolUtil.isNotEmpty(activitiAudit) && ToolUtil.isNotEmpty(activitiAudit.getRule()) && activitiProcessLogService.checkUser(activitiAudit.getRule(), taskId)) {
                        taskResult.setPermissions(true);
                        break;
                    }
                }
            }

            taskResult.setStepsResult(stepResult);
        }

        List comments = remarksService.getComments(taskId);
        taskResult.setRemarks(comments);

        if (ToolUtil.isNotEmpty(taskResult.getCreateUser())) {
            User user = userService.getById(taskResult.getCreateUser());
            String imgUrl = appStepService.imgUrl(user.getUserId().toString());
            user.setAvatar(imgUrl);
            taskResult.setUser(user);

        }
        return ResponseData.success(taskResult);

    }


    private ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId) {
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
    }

    @RequestMapping(value = "/canOperat", method = RequestMethod.POST)
    public ResponseData canOperat(@RequestBody ActivitiProcessParam activitiProcessParam) {
        boolean b = logService.canOperat(activitiProcessParam.getType(), activitiProcessParam.getModule(), activitiProcessParam.getAction());
        return ResponseData.success(b);
    }
}

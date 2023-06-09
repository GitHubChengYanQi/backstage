package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditParam;
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
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
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
public class taskV1Controller {


    @Autowired
    private ActivitiProcessLogV1Service activitiProcessLogService;

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
    private ActivitiProcessLogV1Service logService;

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
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private ShopCartService shopCartService;

    @ApiVersion("1.1")

    @RequestMapping(value = "/{v}/post", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData audit(@RequestBody AuditParam auditParam) {

        //任务添加动态
        shopCartService.auditDynamic(auditParam.getTaskId(), auditParam.getStatus());

        //添加备注
        remarksService.addNote(auditParam);
        this.activitiProcessLogService.judgeLog(auditParam.getTaskId(), auditParam.getLogIds());  //判断当前log状态
        this.activitiProcessLogService.audit(auditParam.getTaskId(), auditParam.getStatus());

        return ResponseData.success();
    }

    @ApiVersion("1.1")
    @RequestMapping(value = "/{v}/detail", method = RequestMethod.GET)
    public ResponseData detail(@Param("taskId") Long taskId) {
        //流程任务
        if (ToolUtil.isEmpty(taskId)) {
            throw new ServiceException(500, "缺少taskId");
        }
        ActivitiProcessTask processTask = taskService.getById(taskId);
        if (ToolUtil.isEmpty(processTask)) {
            return ResponseData.success();
        }
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
                    if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
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
                    InstockOrderResult instockOrderResult = (InstockOrderResult) taskResult.getReceipts();
                    instockOrderService.formatResult(instockOrderResult);
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
        List<ActivitiProcessLog> allUnAuditLog = new ArrayList<>();
        allUnAuditLog.addAll(activitiProcessLogService.getAudit3(taskId));
        if (allUnAuditLog.size() == 0) {
            allUnAuditLog.addAll(activitiProcessLogService.getAudit1(taskId));
        }

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
            for (ActivitiProcessLog activitiProcessLog : allUnAuditLog) {
                if (activitiProcessLog.getStatus() == 3 || activitiProcessLog.getStatus() == -1) {
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
            List<UserResult> userResultsByIds = userService.getUserResultsByIds(new ArrayList<Long>() {{
                add(taskResult.getCreateUser());
            }});

            taskResult.setUser(userResultsByIds.get(0));
        }
        if (ToolUtil.isNotEmpty(taskResult.getOrigin())) {
            taskResult.setThemeAndOrigin(getOrigin.getOrigin(JSON.parseObject(taskResult.getOrigin(), ThemeAndOrigin.class)));
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


}

package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.auditView.service.AuditViewService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.erp.service.impl.CheckInstock;
import cn.atsoft.dasheng.erp.service.impl.CheckQualityTask;
import cn.atsoft.dasheng.erp.service.impl.ProcessTaskEndSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.service.InquiryTaskService;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.purchase.service.impl.CheckPurchaseAsk;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 流程日志表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiProcessLogServiceImpl extends ServiceImpl<ActivitiProcessLogMapper, ActivitiProcessLog> implements ActivitiProcessLogService {

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiProcessTaskSend taskSend;

    @Autowired
    private CheckQualityTask checkQualityTask;

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
        this.auditPerson(taskId, status);
    }

    @Override
    public void autoAudit(Long taskId, Integer status) {
        if (ToolUtil.isEmpty(status)) {
            status = 1;
        }
        this.auditPerson(taskId, status);
    }


    private void setStatus(List<ActivitiProcessLog> logs, Long logId) {
        for (ActivitiProcessLog activitiProcessLog : logs) {
            if (logId.equals(activitiProcessLog.getLogId())) {
                activitiProcessLog.setStatus(1);
            }
        }
    }

    public void auditPerson(Long taskId, Integer status) {
        if (ToolUtil.isEmpty(status)) {
            throw new ServiceException(500, "请填写审核状态");
        }
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "没有找到该任务，无法进行审批");
        }

        List<ActivitiProcessLog> logs = listByTaskId(taskId);
        List<ActivitiProcessLog> audit = this.getAudit(taskId);
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
            in("setps_id", stepsIds);
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
                                updateStatus(activitiProcessLog.getLogId(), status);
                                setStatus(logs, activitiProcessLog.getLogId());
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
                                updateStatus(activitiProcessLog.getLogId(), status);
                                setStatus(logs, activitiProcessLog.getLogId());
                                //拒绝走拒绝方法
                                if (status.equals(0)) {
                                    this.refuseTask(task);
                                    auditCheck = false;
                                }
                            } else {
                                auditCheck = false;
                            }
                            break;
                        case "instockError":   //入库异常
                            if (checkInstock.checkTask(task.getFormId(), activitiAudit.getRule().getType())) {
                                updateStatus(activitiProcessLog.getLogId(), status);
                                setStatus(logs, activitiProcessLog.getLogId());
                                //拒绝走拒绝方法
                                if (status.equals(0)) {
                                    this.refuseTask(task);
                                    auditCheck = false;
                                }
                            } else {
                                auditCheck = false;
                            }
                            break;
                        default:

                    }
                } else {
                    updateStatus(activitiProcessLog.getLogId(), status);
                    setStatus(logs, activitiProcessLog.getLogId());
                }
            } else {
                //判断权限  筛选对应log
                if (this.checkUser(activitiAudit.getRule())) {
                    updateStatus(activitiProcessLog.getLogId(), status);
                    setStatus(logs, activitiProcessLog.getLogId());
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
                    processLogs = updateSupper(allSteps, logs, step);
                }
            }
            for (ActivitiProcessLog processLog : processLogs) {
                processLog.setStatus(status);
            }

            this.updateBatchById(processLogs);
        }
        /**
         * 自动审批 抄送节点以及判断上级更新上级路由与分支
         */
        loopNext(task, activitiAudits, allSteps, auditCheck);
        /**
         * 流程结束需要重新获取需要审批的节点
         */
        audit = this.getAudit(taskId);
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
                endProcessTask.setStatus(0);
                //更新任务状态
                activitiProcessTaskService.updateById(endProcessTask);
                //更新任务关联单据状态
                this.updateStatus(task);
            }
            //推送流程结束消息
            endSend.endSend(task.getProcessTaskId());
        }
    }

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
            case "inquiry":
                inquiryTaskService.updateStatus(processTask);
                break;
            case "instockError":
                instockOrderService.updateStatus(processTask);
                break;
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
            case "inquiry":
                inquiryTaskService.updateRefuseStatus(processTask);
                break;
            case "instockError":
                instockOrderService.updateRefuseStatus(processTask);
                break;
        }
    }

    private void refuseTask(ActivitiProcessTask processTask) {
        processTask.setStatus(0);
        activitiProcessTaskService.updateById(processTask);
        this.updateRefuseStatus(processTask);
        taskSend.refuseTask(processTask.getProcessTaskId());
    }

    private void loopNext(ActivitiProcessTask task, List<ActivitiAudit> activitiAuditList, List<ActivitiSteps> allSteps, Boolean auditCheck) {

        List<ActivitiProcessLog> audit = this.getAudit(task.getProcessTaskId());

        if (auditCheck) {
            this.sendNextStepsByTask(task, audit);
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
                updateStatus(activitiProcessLog.getLogId(), 1);

                ActivitiSteps activitiSteps = getSteps(allSteps, activitiProcessLog.getSetpsId());
                List<ActivitiProcessLog> processLogs = updateSupper(allSteps, logs, activitiSteps);


                if (processLogs.size() > 0) {
                    for (ActivitiProcessLog processLog : processLogs) {
                        processLog.setStatus(1);
                    }
                    this.updateBatchById(processLogs);
                    loopNext(task, activitiAuditList, allSteps, auditCheck);
                }
            }
        }
    }

    private void updateStatus(Long logId, Integer status) {
        ActivitiProcessLog entity = new ActivitiProcessLog();
        entity.setStatus(status);
        entity.setLogId(logId);
        this.updateById(entity);
    }


    /**
     * 确认质检任务状态 （未完成 0）/（完成 1）/（待入库 2）
     *
     * @param
     * @return
     */


    private List<ActivitiProcessLog> updateSupper(List<ActivitiSteps> steps, List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps) {
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
                                if (processLog.getSetpsId().equals(log.getSetpsId())) {
                                    processLog.setStatus(1);
                                }
                            }
                            if (log.getStatus().equals(-1)) {
                                logs.add(log);
                            }
//                            logs.add(log);
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()));
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
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()));
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
                            List<ActivitiProcessLog> routeLog = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()));
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
    public Boolean checkUser(AuditRule starUser) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Long userId = user.getId();
        List<Long> users = taskSend.selectUsers(starUser);
        for (Long aLong : users) {
            if (aLong.equals(userId)) {
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
    public List<ActivitiProcessLog> getAudit(Long taskId) {
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

        ActivitiProcessLog log = getLog(activitiStepsResult.getSetpsId(), activityProcessLog);
        switch (activitiStepsResult.getType()) {
            case START:
            case AUDIT:
                if (ToolUtil.isNotEmpty(log)) {
                    if (log.getStatus().equals(1)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
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
        return activitiStepsResultList;
    }

    private ActivitiProcessLog getLog(Long setpsId, List<ActivitiProcessLog> activityProcessLog) {
        for (ActivitiProcessLog activitiProcessLog : activityProcessLog) {
            if (activitiProcessLog.getSetpsId().equals(setpsId)) {
                return activitiProcessLog;
            }
        }
        return null;
    }

    @Override
    public ActivitiStepsResult addLog(Long processId, Long taskId) {
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);
        loopAdd(activitiStepsResult, taskId);
        viewService.addView(taskId);
        return activitiStepsResult;
    }

    @Override
    public ActivitiStepsResult microAddLog(Long processId, Long taskId,Long userId) {
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);
        loopAdd(activitiStepsResult, taskId);
        viewService.microAddView(taskId,userId);
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

    private void loopAdd(ActivitiStepsResult activitiStepsResult, Long taskId) {

        Long processId = activitiStepsResult.getProcessId();

        /**
         * insert
         */
        ActivitiProcessLog processLog = new ActivitiProcessLog();
        processLog.setPeocessId(processId);
        processLog.setTaskId(taskId);
        processLog.setSetpsId(activitiStepsResult.getSetpsId());
//        if (activitiStepsResult.getType().equals("0")) {
//            processLog.setStatus(1);
//        } else {
        processLog.setStatus(-1);
//        }
        this.save(processLog);

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
    public PageInfo<ActivitiProcessLogResult> findPageBySpec(ActivitiProcessLogParam param) {
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


    public void sendNextStepsByTask(ActivitiProcessTask task, List<ActivitiProcessLog> audit) {
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
                in("setps_id", nextStepsIds);
            }});

            for (ActivitiAudit activitiAudit : activitiAudits) {

                if (ToolUtil.isNotEmpty(activitiAudit) && !activitiAudit.getType().equals("route") && !activitiAudit.getType().equals("branch")) {
                    RuleType ruleType = activitiAudit.getRule().getType();
                    taskSend.send(ruleType, activitiAudit.getRule(), task.getProcessTaskId());
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

        return logResults;
    }


    private Boolean judegSupper(Long setpsId, List<ActivitiSteps> stepsResults, List<ActivitiProcessLog> processLogs) {
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
        processLog.setPeocessId(processId);
        processLog.setTaskId(taskId);
        processLog.setSetpsId(activitiStepsResult.getSetpsId());
        processLog.setStatus(-1);
        //判断分支条件
        if (activitiStepsResult.getType().toString().equals("3")) {
            AuditRule auditRule = activitiStepsResult.getAuditRule();
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
//                    case money:  //总金额
//                        if (!judeg(ask, Long.valueOf(purchaseAsk.getMoney()))) {
//                            b = false;
//                        }
//                        break;
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
        List<Long> stepIds = getStepIdsByType("audit");
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
     * 查出当前跪下的所有步骤
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


    void format(List<ActivitiProcessLogResult> data) {
        List<Long> taskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ActivitiProcessLogResult datum : data) {
            taskIds.add(datum.getTaskId());
            userIds.add(datum.getCreateUser());
        }
        List<ActivitiProcessTask> tasks = activitiProcessTaskService.listByIds(taskIds);
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

}

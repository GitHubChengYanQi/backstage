package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.erp.service.impl.CheckQualityTask;
import cn.atsoft.dasheng.erp.service.impl.ProcessTaskEndSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.internal.bind.DateTypeAdapter;
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
    private ProcessTaskEndSend endSend;

    @Autowired
    private PurchaseAskService askService;


    private ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId) {
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
    }

    private List<ActivitiProcessLog> listByTaskId(Long taskId) {
        return this.list(new QueryWrapper<ActivitiProcessLog>() {{
            eq("task_id", taskId);
        }});
    }

    @Override
    public void audit(Long taskId, Integer status) {
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

//        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
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


            if (!"audit".equals(auditType.toString())) {
                if (activitiAudit.getType().equals("process")) {
                    switch (task.getType()) {
                        case "quality_task":
                            if (checkQualityTask.checkTask(task.getFormId(), activitiAudit.getRule().getType())) {
                                updateStatus(activitiProcessLog.getLogId(), status);
                                setStatus(logs, activitiProcessLog.getLogId());
                                if (status.equals(0)) {
                                    taskSend.refuseTask(taskId);
                                    auditCheck = false;
                                }
                            } else {
                                auditCheck = false;
                            }
                            break;
                        case "aaa"://举例 为以后生产 工艺 做例备忘
                            break;
                        default:

                    }
                } else {
                    updateStatus(activitiProcessLog.getLogId(), status);
                    setStatus(logs, activitiProcessLog.getLogId());
                }
            } else {
                if (this.checkUser(activitiAudit.getRule())) {
                    updateStatus(activitiProcessLog.getLogId(), status);
                    setStatus(logs, activitiProcessLog.getLogId());
                    //判断审批是否通过  不通过推送发起人审批状态  通过 在方法最后发送下一级执行
                    if (status.equals(0)) {
                        taskSend.refuseTask(taskId);
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

        loopNext(task, activitiAudits, allSteps, auditCheck);
        /**
         * 流程结束需要重新获取
         */
        audit = this.getAudit(taskId);
        if (ToolUtil.isEmpty(audit)) {
            /**
             * 流程结束
             */
            ActivitiProcessTask endProcessTask = new ActivitiProcessTask();
            endProcessTask.setProcessTaskId(taskId);
            endProcessTask.setStatus(1);
            activitiProcessTaskService.updateById(endProcessTask);
            endSend.endSend(task.getProcessTaskId());
        }
        /**
         * 推送下一级所有节点
         */
        if (auditCheck) {
            this.sendNextStepsByTask(task, audit);
        }

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
                        if (!log.getStatus().equals(-1)) {
                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId())) {
                                    processLog.setStatus(1);
                                }
                            }
                            logs.add(log);
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
                                if (activitiProcessLog.getSetpsId().toString().equals(branchSteps) && activitiProcessLog.getStatus() != 1) {
                                    state = false;
                                    break;
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
                activitiStepsResultList.add(log);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLog));
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
        return activitiStepsResult;
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
    public void addLogJudgeBranch(Long processId, Long taskId, Long sourId) {
        //TODO 分支添加log
        ActivitiStepsResult stepResult = stepsService.getStepResult(processId);
        PurchaseAsk purchaseAsk = askService.getById(sourId);

        loopAddJudgeBranch(stepResult, taskId, purchaseAsk);
    }

    private void loopAddJudgeBranch(ActivitiStepsResult activitiStepsResult, Long taskId, PurchaseAsk purchaseAsk) {

        Long processId = activitiStepsResult.getProcessId();

        /**
         * insert
         */
        ActivitiProcessLog processLog = new ActivitiProcessLog();
        processLog.setPeocessId(processId);
        processLog.setTaskId(taskId);
        processLog.setSetpsId(activitiStepsResult.getSetpsId());
        processLog.setStatus(-1);
        if (activitiStepsResult.getType().toString().equals("branch")) {
            AuditRule auditRule = activitiStepsResult.getAuditRule();
            boolean b = true;
            for (AuditRule.Rule rule : auditRule.getRules()) {
                AuditRule.PurchaseAsk ask = rule.getPurchaseAsk();

                switch (rule.getType()) {
                    case type_number:
                        b = judeg(ask, purchaseAsk.getTypeNumber());
                        if (!b) {
                            return;
                        }
                    case money:
                        b = judeg(ask, Long.valueOf(purchaseAsk.getMoney()));
                        if (!b) {
                            return;
                        }
                    case number:
                        b = judeg(ask, purchaseAsk.getNumber());
                        if (!b) {
                            return;
                        }
                }

                if (b) {
                    processLog.setStatus(2);
                }
            }
        }
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

    /**
     * 比对
     *
     * @param ask
     * @param number
     * @return
     */
    private Boolean judeg(AuditRule.PurchaseAsk ask, Long number) {
        boolean b;
        switch (ask.getOperator()) {
            case ">":
                b = ask.getValue() > number;
                if (!b) {
                    return false;
                }
            case ">=":
                b = ask.getValue() >= number;
                if (!b) {
                    return false;
                }
            case "===":
                b = ask.getValue().toString().equals(number.toString());
                if (!b) {
                    return false;
                }
            case "<":
                b = ask.getValue() < number;
                if (!b) {
                    return false;
                }
            case "<=":
                b = ask.getValue() <= number;
                if (!b) {
                    return false;
                }
            case "!=":
                b = !ask.getValue().toString().equals(number.toString());
                if (!b) {
                    return false;
                }

        }
        return false;
    }


}

package cn.atsoft.dasheng.audit.service.impl;


import cn.atsoft.dasheng.audit.entity.ActivitiAuditV2;
import cn.atsoft.dasheng.audit.entity.ActivitiProcessLogDetail;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogDetailService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.audit.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.FormFieldParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResultV2;
import cn.atsoft.dasheng.form.pojo.*;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.impl.ActivitiProcessTaskServiceImpl;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 流程日志表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiProcessFormLogServiceImpl extends ServiceImpl<ActivitiProcessLogMapper, ActivitiProcessLog> implements ActivitiProcessFormLogService {
    @Autowired
    private ActivitiProcessTaskServiceImpl activitiProcessTaskService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiProcessFormLogDetailService logDetailService;

    @Override
    public void add(ActivitiProcessLogParam param) {
        ActivitiProcessLog entity = getEntity(param);

        ActivitiAudit audit = auditService.query().eq("setps_id", param.getSetpsId()).one();
//        QualityRules bean = JSONUtil.toBean(audit.getRule(), QualityRules.class);
        List<Long> users = new ArrayList<>();


        boolean flag2 = users.contains(LoginContextHolder.getContext().getUserId());
        int admin = activitiProcessTaskService.isAdmin(param.getTaskId());
        if (admin == 0 && flag2 == false) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        } else {
            this.save(entity);
        }
    }

    @Override

    public void audit(Long taskId, Integer status, Long userId) {
        //获取任务单据
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "没有找到该任务，无法进行审批");
        }

        Long processId = task.getProcessId();
        //获取所有log
        List<ActivitiProcessLog> logs = listByTaskId(taskId);

        //获取需要审批的logs
        List<ActivitiProcessLog> auditLogList = getAudit(taskId);

        boolean auditCheck = true;

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
        List<ActivitiAuditV2> activitiAudits = auditService.listByStepsIds(stepsIds);

        //所有节点
        List<ActivitiSteps> allSteps = stepsService.listByIds(stepsIds);

        //根据节点集合去出 节点审批人
        List<ActivitiProcessLogDetail> logDetailList = this.logDetailService.listByLogIds(auditLogList.stream().map(ActivitiProcessLog::getLogId).collect(Collectors.toList()));
        //校验权限
        if (ToolUtil.isNotEmpty(userId) && logDetailList.stream().noneMatch(i -> i.getUserId().equals(userId))) {
            throw new ServiceException(500, "您不在审批人员列表内,无权审批");
        }
        /**
         * 审批节点循环
         */
        for (ActivitiProcessLog activitiProcessLog : auditLogList) {
            //获取节点规则
            ActivitiAuditV2 activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());
            /**
             * 跳过分支和路由
             */
            if (activitiAudit.getType().equals("branch") || activitiAudit.getType().equals("route")) {
                continue;
            }
            for (ActivitiProcessLogDetail logDetail : logDetailList) {
                if (logDetail.getUserId().equals(userId) && logDetail.getLogId().equals(activitiProcessLog.getLogId())) {
                    logDetail.setStatus(status);
                }
            }
            /**
             * 审批状态更新
             */
            if (status.equals(1)) {
                List<FormFieldParam> formFieldParams = JSON.parseArray(JSON.toJSONString(activitiAudit.getRule()), FormFieldParam.class);
                if (ToolUtil.isNotEmpty(formFieldParams) && formFieldParams.stream().anyMatch(i -> i.getName().equals("action"))) {
                    for (FormFieldParam formFieldParam : formFieldParams) {
                        if (formFieldParam.getName().equals("action")) {
                            if (formFieldParam.getValue().equals("1")) {
//                                并签
                                updateStatus(activitiProcessLog.getLogId(), status, userId);
                                setStatus(logs, activitiProcessLog.getLogId());
                                //更新节点其他人员审批状态
                                for (ActivitiProcessLogDetail logDetail : logDetailList) {
                                    if (logDetail.getStatus().equals(-1) && logDetail.getLogId().equals(activitiProcessLog.getLogId())) {
                                        logDetail.setStatus(4);
                                    }
                                }
                            } else if (formFieldParam.getValue().equals("2")) {
//                                或签
                                List<ActivitiProcessLogDetail> stepLogDetailList = new ArrayList<>();
                                for (ActivitiProcessLogDetail logDetail : logDetailList) {
                                    //统计该节点下所有审批人的审批状态
                                    if (logDetail.getLogId().equals(activitiProcessLog.getLogId())) {
                                        stepLogDetailList.add(logDetail);
                                    }
                                }
                                //如果全部审批完成则更新节点状态
                                if (stepLogDetailList.stream().allMatch(i -> i.getStatus().equals(1))) {
                                    updateStatus(activitiProcessLog.getLogId(), status, userId);
                                    setStatus(logs, activitiProcessLog.getLogId());
                                }
                            }

                        }
                    }
                }

            } else if (status.equals(0)) {
                this.refuseTask(task);
                auditCheck = false;
            }
        }
        /**
         * 自动审批 抄送节点以及判断上级更新上级路由与分支
         */
        loopNext(task, activitiAudits, allSteps, auditCheck, userId);

        /**
         * 从新获取下级
         */
        auditLogList = this.getAudit(taskId);
        logDetailService.updateBatchById(logDetailList);

        //如果查询下级需要审批节点  为空  则审批流程已经没有了 下级节点  则流程结束
        if (ToolUtil.isEmpty(auditLogList)) {

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
//                this.updateStatus(task);
                /**
                 * 如果是子单据  子单据完成  反向去更新父级状态
                 */
//                this.updateParentProcessTask(task, loginUserId);
            }
            //推送流程结束消息
//            endSend.endSend(task.getProcessTaskId());
        } else {
            /**
             * 更新待审批节点状态
             */
            for (ActivitiProcessLog activitiProcessLog : auditLogList) {
                activitiProcessLog.setStatus(3);
            }
            this.updateBatchById(auditLogList);
        }


    }

    /**
     * 审批否决
     *
     * @param processTask
     */
    private void refuseTask(ActivitiProcessTask processTask) {
        processTask.setStatus(50);
        activitiProcessTaskService.updateById(processTask);
//        taskSend.refuseTask(processTask.getProcessTaskId());
    }

    private void setStatus(List<ActivitiProcessLog> logs, Long logId) {
        for (ActivitiProcessLog activitiProcessLog : logs) {
            if (logId.equals(activitiProcessLog.getLogId())) {
                activitiProcessLog.setStatus(1);
            }
        }
    }

    private void loopNext(ActivitiProcessTask task, List<ActivitiAuditV2> activitiAuditList, List<ActivitiSteps> allSteps, Boolean auditCheck, Long loginUserId) {

        List<ActivitiProcessLog> audit = this.getAudit(task.getProcessTaskId());

        if (auditCheck) {
//            this.sendNextStepsByTask(task, audit, loginUserId);
        }
        List<ActivitiProcessLog> logs = listByTaskId(task.getProcessTaskId());

        /**
         * 更新下一个审批节点之前的所有抄送
         */
        for (ActivitiProcessLog activitiProcessLog : audit) {
            /**
             * 取节点规则
             */
            ActivitiAuditV2 activitiAudit = getRule(activitiAuditList, activitiProcessLog.getSetpsId());

            if (ToolUtil.isNotEmpty(activitiAudit) && activitiAudit.getType().equals("send")) {
                updateStatus(activitiProcessLog.getLogId(), 1, loginUserId);

                ActivitiSteps activitiSteps = getSteps(allSteps, activitiProcessLog.getSetpsId());
                List<ActivitiProcessLog> processLogs = updateSupper(allSteps, logs, activitiSteps, loginUserId);


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

    private ActivitiSteps getSteps(List<ActivitiSteps> steps, Long stepId) {
        for (ActivitiSteps step : steps) {
            if (step.getSetpsId().toString().equals(stepId.toString())) {
                return step;
            }
        }
        return null;
    }

    /**
     * 更新上级节点
     *
     * @param steps
     * @param processLogs
     * @param activitiSteps
     * @param loginUser
     * @return
     */
    private List<ActivitiProcessLog> updateSupper(List<ActivitiSteps> steps, List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps, Long loginUser) {
        List<ActivitiProcessLog> logs = new ArrayList<>();

        for (ActivitiSteps step : steps) {
            // 取当前步骤上级
            if (step.getSetpsId().equals(activitiSteps.getSetpsId())) {
                ActivitiProcessLog log = getLog(processLogs, activitiSteps);
                switch (step.getType()) {
                    case audit:
                    case send:
                        //TODO 原判断 if (log.getStatus().equals(1))
                        if (log.getStatus().equals(1)) {
                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId()) && ((ToolUtil.isNotEmpty(processLog.getAuditUserId()) && processLog.getAuditUserId().equals(loginUser)) || ToolUtil.isEmpty(processLog.getAuditUserId()))) {
                                    processLog.setStatus(1);
                                }
                            }
                            if (log.getStatus().equals(-1) || log.getStatus().equals(3)) {
                                logs.add(log);
                            }
//                            logs.add(log);
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()), loginUser);
                            logs.addAll(newLogs);
                        }
                        break;
                    case branch:
                        // 添加当前步骤log
                        if (judgeNext(processLogs, steps, activitiSteps)) {

                            for (ActivitiProcessLog processLog : processLogs) {
                                if (processLog.getSetpsId().equals(log.getSetpsId())) {
                                    processLog.setStatus(1);
                                }
                            }
                            logs.add(log);
                            // 下级完成递归传入当前步骤判断上一级
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()), loginUser);
                            logs.addAll(newLogs);
                        }
                        break;

                    case route:

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
                            List<ActivitiProcessLog> routeLog = updateSupper(steps, processLogs, getSteps(steps, step.getSupper()), loginUser);
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
                case audit:
                case send:
                case route:
                    return processLog.getStatus() == 1;
                default:
                    return true;
            }

        }

        return true;
    }

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

    /**
     * 更新log节点
     *
     * @param logId
     * @param status
     * @param loginUserId
     */
    private void updateStatus(Long logId, Integer status, Long loginUserId) {
        ActivitiProcessLog entity = new ActivitiProcessLog();
        entity.setStatus(status);
        entity.setLogId(logId);
        entity.setUpdateUser(loginUserId);
        this.updateById(entity);
    }

    public ActivitiAuditV2 getRule(List<ActivitiAuditV2> activitiAudits, Long stepId) {
        for (ActivitiAuditV2 activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
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
        ActivitiStepsResultV2 activitiStepsResult = stepsService.backStepsResultV2(task.getProcessId());
        List<ActivitiProcessLog> activitiProcessLogs = listByTaskId(taskId);
        return loopAudit(activitiStepsResult, activitiProcessLogs);
    }


    private List<ActivitiProcessLog> loopAudit(ActivitiStepsResultV2 activitiStepsResult, List<ActivitiProcessLog> activityProcessLogList) {
        List<ActivitiProcessLog> activitiStepsResultList = new ArrayList<>();

        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return activitiStepsResultList;
        }

        ActivitiProcessLog log = getLog(activitiStepsResult.getSetpsId(), activityProcessLogList);
        switch (activitiStepsResult.getType()) {
            case start:
            case process:
                if (ToolUtil.isNotEmpty(log)) {
                    if (log.getStatus().equals(1) || log.getStatus().equals(4)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLogList));
                    } else {
                        activitiStepsResultList.add(log);
                    }
                }
                break;
            case send:
                if (ToolUtil.isNotEmpty(log)) {
                    if (!log.getStatus().equals(1)) {
                        activitiStepsResultList.add(log);
                    }
                    if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLogList));
                    }
                }
                break;
            case branch:
                if (ToolUtil.isNotEmpty(log)) {
                    if (!log.getStatus().equals(2)) {
                        activitiStepsResultList.add(log);
                        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                            activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLogList));
                        }
                    }
                }
                break;
            case route:
                if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
                    if (ToolUtil.isNotEmpty(log) && log.getStatus().equals(1)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activityProcessLogList));
                    } else {
                        activitiStepsResultList.add(log);
                        for (ActivitiStepsResultV2 stepsResult : activitiStepsResult.getConditionNodeList()) {
                            activitiStepsResultList.addAll(loopAudit(stepsResult, activityProcessLogList));
                        }
                    }

                }
                break;
        }
        return activitiStepsResultList;
    }

    //匹配log节点
    private ActivitiProcessLog getLog(Long setpsId, List<ActivitiProcessLog> activityProcessLog) {
        for (ActivitiProcessLog activitiProcessLog : activityProcessLog) {
            if (activitiProcessLog.getSetpsId().equals(setpsId)) {
                return activitiProcessLog;
            }
        }
        return null;
    }

    private ActivitiProcessLog getLog(List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps) {

        for (ActivitiProcessLog processLog : processLogs) {
            // 取当前步骤log
            if (processLog.getSetpsId().equals(activitiSteps.getSetpsId())) {
                return processLog;

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
    public void add(Long processId, Long taskId) {
        ActivitiStepsResultV2 activitiStepsResultV2 = stepsService.backStepsResultV2(processId);
        this.loopAddLogs(activitiStepsResultV2, taskId, 0L);
    }

    private void loopAddLogs(ActivitiStepsResultV2 steps, Long taskId, Long parentLogId) {
        Long processId = steps.getProcessId();

        /**
         * insert
         */
        ActivitiProcessLog processLog = new ActivitiProcessLog();
        processLog.setPeocessId(processId);
        processLog.setTaskId(taskId);
        processLog.setSetpsId(steps.getSetpsId());
        if (steps.getType().equals(StepsType.route)) {
            processLog.setRouteLogId(parentLogId);
        }
        processLog.setStatus(-1);
        //TODO 更改参数格式
//        if (ToolUtil.isNotEmpty(steps.getAuditRule()) && ToolUtil.isNotEmpty(steps.getAuditRule().getActionStatuses())) {
//            List<ActionStatus> actionStatuses = steps.getAuditRule().getActionStatuses();
//            if (ToolUtil.isNotEmpty(actionStatuses)) {
//                for (ActionStatus actionStatus : actionStatuses) {
//                    actionStatus.setStatus(0);
//                }
//                processLog.setActionStatus(JSON.toJSONString(actionStatuses));
//            }
//        }
        List<Long> userIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty((steps.getRoleList()))) {

            List<FormFieldParam> formFieldParams = JSON.parseArray(JSON.toJSONString(steps.getRoleList()), FormFieldParam.class);
            for (FormFieldParam formFieldParam : formFieldParams) {
                switch (formFieldParam.getName()) {
                    case "documentsStatus":
                        AuditRule value = (AuditRule) formFieldParam.getValue();
                        List<ActionStatus> actionStatuses = value.getActionStatuses();
                        if (ToolUtil.isNotEmpty(actionStatuses)) {
                            for (ActionStatus actionStatus : actionStatuses) {
                                actionStatus.setStatus(0);
                            }

                            processLog.setActionStatus(JSON.toJSONString(actionStatuses));


                        }
                        break;
                    case "rules":

                        List<AuditRule.Rule> rules = JSON.parseArray(JSON.toJSONString(formFieldParam.getValue()), AuditRule.Rule.class);
                        userIds = selectUsers(rules, taskId);
                        break;
                }
            }
        }

        this.save(processLog);
        logDetailService.addByLog(processLog, userIds);
        if (ToolUtil.isNotEmpty(steps.getConditionNodeList()) && steps.getConditionNodeList().size() > 0) {
            for (ActivitiStepsResultV2 stepsResult : steps.getConditionNodeList()) {
                loopAddLogs(stepsResult, taskId, processLog.getLogId());
            }
        }
        if (ToolUtil.isNotEmpty(steps.getChildNode())) {
            loopAddLogs(steps.getChildNode(), taskId, processLog.getLogId());
        }
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
        ActivitiProcessLog oldEntity = getOldEntity(param);
        ActivitiProcessLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        int admin = activitiProcessTaskService.isAdmin(oldEntity.getTaskId());
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        } else {
            this.updateById(newEntity);
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

    /**
     * 根据传入参数换取 节点里面包含人的条件规则 找到人
     *
     * @param ruleList
     * @return
     */
    public List<Long> selectUsers(List<AuditRule.Rule> ruleList, Long taskId) {
        List<Long> users = new ArrayList<>();

        for (AuditRule.Rule rule : ruleList) {
            switch (rule.getType()) {
                case AppointUsers:
                    for (AppointUser appointUser : rule.getAppointUsers()) {
                        users.add(Long.valueOf(appointUser.getKey()));
                    }
                    break;
                case AllPeople:
                    List<Long> allUsersId = userService.getAllUsersId();
                    users.addAll(allUsersId);
                    break;
                case DeptPositions:
                    for (DeptPosition deptPosition : rule.getDeptPositions()) {
                        List<Long> positionIds = new ArrayList<>();
                        for (DeptPosition.Position position : deptPosition.getPositions()) {
                            if (ToolUtil.isNotEmpty(position.getValue())) {
                                positionIds.add(Long.valueOf(position.getValue()));
                            }
                        }
                        List<User> userByPositionAndDept = userService.getUserByPositionAndDept(Long.valueOf(deptPosition.getKey()), positionIds);
                        for (User user : userByPositionAndDept) {
                            users.add(user.getUserId());
                        }
                    }
                    break;
                case MasterDocumentPromoter:    //主单据发起人
                    if (ToolUtil.isNotEmpty(taskId)) {
                        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
                        if (ToolUtil.isNotEmpty(processTask.getMainTaskId())) {
                            ActivitiProcessTask mainTask = activitiProcessTaskService.getById(processTask.getMainTaskId());
                            if (ToolUtil.isNotEmpty(mainTask)) {
                                users.add(mainTask.getCreateUser());
                            }
                        }
//                        if (processTask.getType().equals("INSTOCKERROR")) {
//                            AnomalyOrder anomalyOrder = anomalyOrderService.getById(processTask.getFormId());
//                            InstockOrder instockOrder = instockOrderService.getById(anomalyOrder);
//                            users.add(instockOrder.getCreateUser());
//                        }
                    }
                    break;
                case Director:
                    if (ToolUtil.isNotEmpty(taskId)) {
                        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
                        if (ToolUtil.isNotEmpty(processTask) && ToolUtil.isNotEmpty(processTask.getUserId())) {
                            users.add(processTask.getUserId());
                        }
                    }
                    break;
            }
        }

        return users;
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

    @Override
    public void checkAction(Long id, String formType, Long actionId, Long loginUserId) {

        ActivitiProcessTask processTask = activitiProcessTaskService.query().eq("type", formType).eq("form_id", id).eq("display", 1).one();
        if (ToolUtil.isEmpty(processTask)) {
            throw new ServiceException(500, "当前任务不存在");
        }
        List<ActivitiProcessLog> logs = this.getAudit(processTask.getProcessTaskId());
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : logs) {
            stepIds.add(processLog.getSetpsId());
        }
        List<ActivitiSteps> activitiStepList = stepIds.size() == 0 ? new ArrayList<>() : stepsService.listByIds(stepIds);
        for (ActivitiProcessLog processLog : logs) {
            for (ActivitiSteps step : activitiStepList) {
                if (processLog.getSetpsId().equals(step.getSetpsId()) && step.getStepType().equals("status") && (ToolUtil.isEmpty(processLog.getAuditUserId()) || processLog.getAuditUserId().equals(loginUserId))) {
                    this.checkLogActionComplete(processTask.getProcessTaskId(), step.getSetpsId(), actionId, loginUserId);
                    return;
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
            //TODO 可以将其他人的JSON一并更改
            if ((ToolUtil.isEmpty(processLog.getAuditUserId()) || processLog.getAuditUserId().equals(loginUserId)) && ToolUtil.isNotEmpty(processLog.getActionStatus())) {
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


                actionStatuses.removeIf(i -> !i.getChecked());
                if (actionStatuses.stream().allMatch(i -> i.getStatus().equals(1) && i.getChecked())) {
                    this.audit(taskId, 1, loginUserId);

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
//            }
            }

        }
    }
}

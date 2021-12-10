package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.Tool.TypeFromService;
import cn.atsoft.dasheng.Tool.WxCpSend;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.erp.service.impl.CheckQualityTask;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;

import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.pojo.StepsType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.form.pojo.StepsType.*;

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
    private QualityTaskService qualityTaskService;

    @Autowired
    private CheckQualityTask checkQualityTask;

    @Autowired
    private UserService userService;

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
    public void autoAudit(Long taskId) {
        this.auditPerson(taskId, 1);
    }

    public void auditPerson(Long taskId, Integer status) {

        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "没有找到该任务，无法进行审批");
        }

//        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
        List<ActivitiProcessLog> logs = listByTaskId(taskId);
        List<ActivitiProcessLog> audit = this.getAudit(task.getProcessId(), logs);
        /**
         * 流程中审核节点
         */
        List<Long> stepsIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : audit) {
            stepsIds.add(processLog.getSetpsId());
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
//
                            if (checkQualityTask.checkTask(task.getFormId(),activitiAudit.getRule().getType())){
                                updateStatus(activitiProcessLog.getLogId(), 1);
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
                }
            } else {
                if (this.checkUser(activitiAudit.getRule())) {
                    updateStatus(activitiProcessLog.getLogId(), status);
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
                    processLogs = updateSupper(allSteps, logs, step, step.getSetpsId());
                }
            }
            for (ActivitiProcessLog processLog : processLogs) {
                processLog.setStatus(status);
            }

            this.updateBatchById(processLogs);
        }


        if (auditCheck) {
            this.sendNextStepsByTask(task);
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


    private List<ActivitiProcessLog> updateSupper
    (List<ActivitiSteps> steps, List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps, Long stepId) {
        List<ActivitiProcessLog> logs = new ArrayList<>();


        for (ActivitiSteps step : steps) {
            // 取当前步骤上级
            if (step.getSetpsId().equals(activitiSteps.getSupper())) {


                ActivitiProcessLog log = getLastLog(processLogs, activitiSteps);

                switch (step.getType()) {
                    case AUDIT:
                    case SEND:
                    case BRANCH:
                        // 添加当前步骤log
                        if (judgeNext(processLogs, steps, activitiSteps, stepId)) {
                            logs.add(log);
                            // 下级完成递归传入当前步骤判断上一级
                            List<ActivitiProcessLog> newLogs = updateSupper(steps, processLogs, step, stepId);
                            logs.addAll(newLogs);
                        }
                        return logs;
                    case ROUTE:
                        String[] split = step.getConditionNodes().split(",");

                        List<String> noUpdate = new ArrayList<>();
                        List<String> update = new ArrayList<>();

                        for (String s : split) {
                            if (!activitiSteps.getSetpsId().toString().equals(s)) {
                                for (ActivitiProcessLog processLog : processLogs) {
                                    if (processLog.getSetpsId().toString().equals(s) && processLog.getStatus() == 1) {
                                        update.add(s);
                                    }
                                }
                            } else {
                                if (judgeNext(processLogs, steps, activitiSteps, stepId)) {
                                    noUpdate.add(s);
                                }
                            }
                        }
                        if ((update.size() == split.length - noUpdate.size())) {
                            logs.add(log);
                            List<ActivitiProcessLog> routeLog = updateSupper(steps, processLogs, step, stepId);
                            logs.addAll(routeLog);
                            return logs;
                        } else {
                            return logs;
                        }

                }

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
            activitiSteps, Long stepId) {

        if (ToolUtil.isEmpty(activitiSteps.getChildren())) {
            return true;
        }
        ActivitiSteps activitiStepsChild = getchildStep(steps, activitiSteps);

        if (ToolUtil.isEmpty(activitiStepsChild)) {
            return true;
        }

        ActivitiProcessLog processLog = getLog(processLogs, activitiStepsChild);

        if (ToolUtil.isNotEmpty(processLog)) {
            switch (activitiStepsChild.getType()) {
                case AUDIT:
                case SEND:
                    if (stepId.equals(processLog.getSetpsId())) {
                        return true;
                    } else {
                        return processLog.getStatus() == 1;
                    }

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
    public List<ActivitiProcessLog> getAudit(Long processId, List<ActivitiProcessLog> activitiProcessLogs) {

        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(processId);

        return loopAudit(activitiStepsResult, activitiProcessLogs);
    }

    private List<ActivitiProcessLog> loopAudit(ActivitiStepsResult
                                                       activitiStepsResult, List<ActivitiProcessLog> activitiProcessLogs) {
        List<ActivitiProcessLog> activitiStepsResultList = new ArrayList<>();

        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return activitiStepsResultList;
        }

        ActivitiProcessLog log = getLog(activitiStepsResult.getSetpsId(), activitiProcessLogs);
        switch (activitiStepsResult.getType()) {
            case START:
            case AUDIT:
            case SEND:
                if (ToolUtil.isNotEmpty(log)) {
                    if (log.getStatus().equals(1)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activitiProcessLogs));
                    } else {
                        activitiStepsResultList.add(log);
                    }
                }
                break;
            case BRANCH:
                activitiStepsResultList.add(log);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activitiProcessLogs));
                }
                break;
            case ROUTE:
                if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
                    if (ToolUtil.isNotEmpty(log) && log.getStatus().equals(1)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activitiProcessLogs));
                    } else {
                        activitiStepsResultList.add(log);
                        for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                            activitiStepsResultList.addAll(loopAudit(stepsResult, activitiProcessLogs));
                        }
                    }

                }
                break;
        }
        return activitiStepsResultList;
    }

    private ActivitiProcessLog getLog(Long setpsId, List<ActivitiProcessLog> activitiProcessLogs) {
        for (ActivitiProcessLog activitiProcessLog : activitiProcessLogs) {
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


    public void sendNextStepsByTask(ActivitiProcessTask task) {
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "未找到相关流程任务");
        }
        List<ActivitiProcessLog> logs = this.listByTaskId(task.getProcessTaskId());
        List<ActivitiProcessLog> audit = this.getAudit(task.getProcessId(), logs);

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
                RuleType ruleType = activitiAudit.getRule().getType();
                if (ToolUtil.isNotEmpty(activitiAudit) && !activitiAudit.getType().equals("route") && !activitiAudit.getType().equals("branch")) {
                    taskSend.send(ruleType, activitiAudit.getRule(), task.getProcessTaskId(), 1);
                }
            }
        }
    }

}

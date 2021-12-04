package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;

import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiProcessTaskSend taskSend;


    private Boolean inUsers(List<QualityRules.Users> users, Long userId) {
        for (QualityRules.Users user : users) {
            if (user.getKey().equals(userId.toString())) {
                return true;
            }
        }
        return false;
    }

    private Boolean inDepts(List<QualityRules.Depts> depts, Long deptId) {
        for (QualityRules.Depts dept : depts) {
            if (dept.getKey().equals(deptId.toString())) {
                return true;
            }
        }
        return false;
    }

    private ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId) {
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
    }


    @Override
    public void add(Long taskId, Integer status) {
        /**
         * 判断status
         */
        List<ActivitiProcessLog> audit = this.getAudit(taskId);

        List<Long> setpsIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : audit) {
            setpsIds.add(processLog.getSetpsId());
        }

        if (ToolUtil.isNotEmpty(setpsIds)) {


            List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", setpsIds);
            }});


            List<ActivitiProcessLog> logs = this.list(new QueryWrapper<ActivitiProcessLog>() {{
                eq("task_id", taskId);
            }});

            List<Long> AllStepdsByLog = new ArrayList<>();
            for (ActivitiProcessLog activitiProcessLog : logs) {
                AllStepdsByLog.add(activitiProcessLog.getSetpsId());
            }

            List<ActivitiSteps> Allsteps = stepsService.listByIds(AllStepdsByLog);


            for (ActivitiProcessLog activitiProcessLog : audit) {
                ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());
                ActivitiSteps activitiSteps = getSteps(Allsteps, activitiProcessLog.getSetpsId());

                if (ToolUtil.isEmpty(activitiAudit) || ToolUtil.isEmpty(activitiSteps)) {
                    continue;
                }

                Long logId = activitiProcessLog.getLogId();
                ActivitiProcessLog entity = new ActivitiProcessLog();
                entity.setSetpsId(activitiProcessLog.getSetpsId());
                entity.setStatus(status);
                entity.setLogId(logId);
                //TODO 未知的 合拼
                List<ActivitiProcessLog> processLogs = new ArrayList<>();


//                    if (inUsers(rule.getQualityRules().getUsers(), loginUser.getId()) || inDepts(rule.getQualityRules().getDepts(), loginUser.getDeptId())) {

                if (activitiSteps.getType().equals(AUDIT)) {

                    if (this.checkUser(activitiAudit.getRule())) {
                        /**
                         * 当前节点更新
                         */
                        entity.setStatus(status);
                        this.updateById(entity);

                        for (ActivitiSteps step : Allsteps) {
                            if (activitiProcessLog.getSetpsId().toString().equals(step.getSetpsId().toString())) {
                                processLogs = updataSupper(Allsteps, logs, step, step.getSetpsId());
                            }
                        }
                        for (ActivitiProcessLog processLog : processLogs) {
                            processLog.setStatus(status);
                        }
                        this.updateBatchById(processLogs);
                    }

                } else if (activitiSteps.getType().equals(SEND)) {
                    entity.setStatus(status);
                    this.updateById(entity);
                    for (ActivitiSteps step : Allsteps) {
                        if (activitiProcessLog.getSetpsId().toString().equals(step.getSetpsId().toString())) {
                            processLogs = updataSupper(Allsteps, logs, step, step.getSetpsId());
                        }
                    }
                    for (ActivitiProcessLog processLog : processLogs) {
                        processLog.setStatus(status);
                    }
                    this.updateBatchById(processLogs);
                } else if (activitiSteps.getType().equals(START)) {
                    entity.setStatus(status);
                    this.updateById(entity);
                }
            }


        }

        /**
         * 所有下一节点送消息的节点
         */
        this.sendNext(taskId);
    }


    private void sendNext(Long taskId) {
        List<ActivitiProcessLog> audit = this.getAudit(taskId);

        List<Long> nextStepsIds = new ArrayList<Long>() {{
            add(0L);
        }};
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);

        for (ActivitiProcessLog activitiProcessLog : audit) {
            nextStepsIds.add(activitiProcessLog.getSetpsId());
        }

//        if (ToolUtil.isNotEmpty(nextStepsIds)) {


        List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", nextStepsIds);
        }});


        for (ActivitiAudit activitiAudit : activitiAudits) {

            if (ToolUtil.isNotEmpty(activitiAudit) && !activitiAudit.getType().equals("route") && !activitiAudit.getType().equals("branch")) {
                taskSend.send(activitiAudit.getType(), activitiAudit.getRule(), task.getProcessTaskId());
            }
        }
//        }
    }

    private List<ActivitiProcessLog> updataSupper
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
                            List<ActivitiProcessLog> newLogs = updataSupper(steps, processLogs, step, stepId);
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
                            List<ActivitiProcessLog> routeLog = updataSupper(steps, processLogs, step, stepId);
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
    private Boolean judgeNext(List<ActivitiProcessLog> processLogs, List<ActivitiSteps> steps, ActivitiSteps activitiSteps, Long stepId) {

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
//        Boolean flag = false;
        List<Long> users = taskSend.selectUsers(starUser);
        for (Long aLong : users) {
            if (aLong.equals(userId)) {
//                flag = true;
                return true;
            }
        }
        return false;
//        if (!flag) {
//            throw new ServiceException(500, "您没有操作权限");
//        }
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
    public List<ActivitiProcessLog> getAudit(Long taskId) {

        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        if (ToolUtil.isEmpty(task)) {
            return new ArrayList<>();
        }
        //查询当前流程所有流程步骤
        ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(task.getProcessId());
        /**
         * 把所有log取出
         */
        List<ActivitiProcessLog> activitiProcessLogs = this.list(new QueryWrapper<ActivitiProcessLog>() {{
            eq("task_id", taskId);
        }});

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
                    if (log.getStatus().equals(1)) {
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

}

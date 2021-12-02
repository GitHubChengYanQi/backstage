package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.ChildAudit;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);

        LoginUser loginUser = LoginContextHolder.getContext().getUser();
        List<ActivitiProcessLog> audit = this.getAudit(taskId);

        List<Long> setpsIds = new ArrayList<>();
        for (ActivitiProcessLog processLog : audit) {
            setpsIds.add(processLog.getSetpsId());
        }

        if (ToolUtil.isNotEmpty(setpsIds)) {

//            ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(task.getProcessId());
//            ActivitiProcess activitiProcess = processService.getById(task.getProcessId());

            List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", setpsIds);
            }});

            List<Long> passSetpIds = new ArrayList<>();

            List<ActivitiSteps> steps = stepsService.listByIds(setpsIds);


            for (ActivitiProcessLog activitiProcessLog : audit) {
                ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());
                if (ToolUtil.isEmpty(activitiAudit)) {
                    continue;
                }
                AuditRule rule = activitiAudit.getRule(); // activitiStepsResult.getAuditRule().getQualityRules();
                String type = activitiAudit.getType();

                Long logId = activitiProcessLog.getLogId();
                ActivitiProcessLog entity = new ActivitiProcessLog();
                entity.setStatus(status);
                entity.setLogId(logId);

                List<ActivitiProcessLog> processLogs = new ArrayList<>();


                if (type.equals("route") || type.equals("branch")) {
                    passSetpIds.add(activitiProcessLog.getSetpsId());
                } else if (ToolUtil.isNotEmpty(rule)) {
                    if (inUsers(rule.getQualityRules().getUsers(), loginUser.getId()) || inDepts(rule.getQualityRules().getDepts(), loginUser.getDeptId())) {
                        for (ActivitiSteps step : steps) {
                            if (activitiProcessLog.getSetpsId().toString().equals(step.getSetpsId().toString())) {
                                processLogs = updataSupper(steps, audit, step);
                            }
                        }
                        for (ActivitiProcessLog processLog : processLogs) {
                            processLog.setStatus(1);
                        }
                        this.updateBatchById(processLogs);
                        /**
                         * 判断操作权限
                         */
                        this.checkUser(activitiAudit.getRule());
                        this.update(entity, new QueryWrapper<ActivitiProcessLog>() {{
                            eq("log_id", entity.getLogId());
                        }});
                        passSetpIds.add(activitiProcessLog.getSetpsId());
                    }
                }
            }
            /**
             * 当前节点发送消息
             */

            /**
             * 所有下一节点送消息的节点
             */

            audit = this.getAudit(taskId);
            List<Long> nextStepsIds = new ArrayList<>();


            for (ActivitiProcessLog activitiProcessLog : audit) {

                nextStepsIds.add(activitiProcessLog.getSetpsId());
            }


            activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", nextStepsIds);
            }});


            for (ActivitiAudit activitiAudit : activitiAudits) {
                if (ToolUtil.isNotEmpty(activitiAudit) && !activitiAudit.getType().equals("route") && !activitiAudit.getType().equals("branch")) {
                    taskSend.send(activitiAudit.getType(), activitiAudit.getRule(), task.getProcessTaskId());
                }
            }
        }
    }


    private List<ActivitiProcessLog> updataSupper(List<ActivitiSteps> steps, List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps) {
        List<ActivitiProcessLog> logs = new ArrayList<>();

        for (ActivitiSteps step : steps) {
            // 取当前步骤上级
            if (step.getSetpsId().equals(activitiSteps.getSupper())) {


                ActivitiProcessLog log = getLog(processLogs, activitiSteps);

                switch (step.getType()) {
                    case "1":
                    case "2":
                    case "3":
                        // 添加当前步骤log
                        logs.add(log);
                        // 递归传入当前步骤
                        List<ActivitiProcessLog> newLogs = updataSupper(steps, processLogs, step);
                        logs.addAll(newLogs);
                        return logs;
                    case "4":
                        String[] split = step.getConditionNodes().split(",");
                        List<String> nodes = new ArrayList<>(Arrays.asList(split));

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
                                noUpdate.add(s);
                            }
                        }
                        if ((update.size() == split.length - noUpdate.size())) {
                            logs.add(log);
                            List<ActivitiProcessLog> routeLog = updataSupper(steps, processLogs, step);
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
     * 比对processLog
     *
     * @param processLogs
     * @param activitiSteps
     * @return
     */
    private ActivitiProcessLog getLog(List<ActivitiProcessLog> processLogs, ActivitiSteps activitiSteps) {

        for (ActivitiProcessLog processLog : processLogs) {
            // 取当前步骤log
            if (processLog.getSetpsId().equals(activitiSteps.getSupper())) {
                return processLog;

            }
        }
        return new ActivitiProcessLog();
    }


    @Override
    public void checkUser(AuditRule starUser) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Long userId = user.getId();
        Long deptId = user.getDeptId();
        Boolean flag = false;
        List<Long> users = taskSend.selectUsers(starUser);
        for (Long aLong : users) {
            if (aLong.equals(userId)) {
                flag = true;
            }
        }
        if (!flag) {
            throw new ServiceException(500, "您没有操作权限");
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

    private List<ActivitiProcessLog> loopAudit(ActivitiStepsResult activitiStepsResult, List<ActivitiProcessLog> activitiProcessLogs) {
        List<ActivitiProcessLog> activitiStepsResultList = new ArrayList<>();

        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return activitiStepsResultList;
        }

        ActivitiProcessLog log = getLog(activitiStepsResult.getSetpsId(), activitiProcessLogs);
        switch (activitiStepsResult.getType()) {
            case "0":
            case "1":
            case "2":
                if (ToolUtil.isNotEmpty(log)) {
                    if (log.getStatus().equals(1)) {
                        activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activitiProcessLogs));
                    } else {
                        activitiStepsResultList.add(log);
                    }
                }
                break;
            case "3":
                activitiStepsResultList.add(log);
                if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
                    activitiStepsResultList.addAll(loopAudit(activitiStepsResult.getChildNode(), activitiProcessLogs));
                }

            case "4":
                if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
                    activitiStepsResultList.add(log);
                    for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                        activitiStepsResultList.addAll(loopAudit(stepsResult, activitiProcessLogs));
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

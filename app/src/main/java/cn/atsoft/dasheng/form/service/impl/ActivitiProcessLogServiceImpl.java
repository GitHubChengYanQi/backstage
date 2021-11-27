package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.ChildAudit;
import cn.atsoft.dasheng.form.pojo.StartUsers;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiProcessTaskSend taskSend;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private UserService userService;


    private Boolean inUsers(List<StartUsers.Users> users, Long userId) {
        for (StartUsers.Users user : users) {
            if (user.getKey().equals(userId.toString())) {
                return true;
            }
        }
        return false;
    }

    private Boolean inDepts(List<StartUsers.Depts> depts, Long deptId) {
        for (StartUsers.Depts dept : depts) {
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
            } else {
                return activitiAudit;
            }
        }
        return null;
    }

    private List<ActivitiStepsResult> getNextNode(ActivitiStepsResult activitiStepsResult, Long stepsId) {
        List<ActivitiStepsResult> results = new ArrayList<>();
        if (ToolUtil.isEmpty(activitiStepsResult)) {
            return results;
        }
        String type = activitiStepsResult.getType();
        if (type.equals("1")) {
            results.add(activitiStepsResult);
            List<ActivitiStepsResult> resultList = getNextNode(activitiStepsResult.getChildNode(), activitiStepsResult.getSetpsId());
            results.addAll(resultList);
            return results;
        } else if (type.equals("2")) {
            results.add(activitiStepsResult);
            List<ActivitiStepsResult> resultList = getNextNode(activitiStepsResult.getChildNode(), activitiStepsResult.getSetpsId());
            results.addAll(resultList);
            return results;
        } else if (type.equals("3")) {
            List<ActivitiStepsResult> resultList = getNextNode(activitiStepsResult.getChildNode(), activitiStepsResult.getSetpsId());
            results.addAll(resultList);
            return results;
        } else if (type.equals("4")) {
            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                List<ActivitiStepsResult> resultList = getNextNode(stepsResult, stepsResult.getSetpsId());
                results.addAll(resultList);
            }
            return results;
        } else if (!activitiStepsResult.getSetpsId().equals(stepsId)) {
            List<ActivitiStepsResult> resultList = getNextNode(activitiStepsResult.getChildNode(), stepsId);
            results.addAll(resultList);
            return results;
        } else {
            results.add(activitiStepsResult);
            return results;
        }
    }

    @Override
    public void add(Long taskId, Integer status, Long stepId) {
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

            ActivitiStepsResult activitiStepsResult = stepsService.backStepsResult(task.getProcessId());
            ActivitiProcess activitiProcess = processService.getById(task.getProcessId());
//            List<ActivitiSteps> activitiSteps = stepsService.listByIds(setpsIds);
//
//            for (ActivitiSteps activitiStep : activitiSteps) {
//
//            }


            List<ActivitiAudit> activitiAudits = this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", setpsIds);
            }});

            List<Long> passSetpIds = new ArrayList<>();
            for (ActivitiProcessLog activitiProcessLog : audit) {

                ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());
                if (ToolUtil.isEmpty(activitiAudit)) {
                    continue;
                }
                AuditRule rule = activitiAudit.getRule(); // activitiStepsResult.getAuditRule().getStartUsers();
                String type = activitiAudit.getType();

                Long logId = activitiProcessLog.getLogId();
                ActivitiProcessLog entity = new ActivitiProcessLog();
                entity.setStatus(status);
                entity.setLogId(logId);

                if (type.equals("luYou") || type.equals("branch")) {
                    this.updateById(entity);
                    passSetpIds.add(activitiProcessLog.getSetpsId());

                } else if (ToolUtil.isNotEmpty(rule)) {

                    if (inUsers(rule.getStartUsers().getUsers(), loginUser.getId()) || inDepts(rule.getStartUsers().getDepts(), loginUser.getDeptId())) {
                        this.updateById(entity);

                        taskSend.logAddSend(activitiAudit.getType(), activitiAudit.getRule(), activitiProcess.getUrl(), activitiAudit.getSetpsId().toString(), task.getProcessTaskId());
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
            List<ActivitiStepsResult> activitiStepsResults = new ArrayList<>();
            for (Long passSetpId : passSetpIds) {
                activitiStepsResults = getNextNode(activitiStepsResult, passSetpId);

            }

            for (ActivitiStepsResult stepsResult : activitiStepsResults) {
//                ActivitiAudit activitiAudit = getRule(activitiAudits, stepsResult.getSetpsId());
                ActivitiAudit setpsId = auditService.query().eq("setps_id", stepsResult.getSetpsId()).one();
                if (stepsResult.getSetpsId().equals(stepId)) {
                    taskSend.logAddSend(setpsId.getType(), setpsId.getRule(), activitiProcess.getUrl(), stepsResult.getChildren(), task.getProcessTaskId());
                }
            }

//            for (ActivitiStepsResult stepsResult : activitiStepsResults) {
//                ActivitiAudit activitiAudit = getRule(activitiAudits, stepsResult.getSetpsId());
//                taskSend.logAddSend(activitiAudit.getType(), activitiAudit.getRule(), activitiProcess.getUrl(), stepsResult.getChildren(), task.getProcessTaskId());
//            }


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
//        ActivitiSteps steps = stepsService.query().eq("process_id", processId).eq("supper", 0).one();
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
        if (activitiStepsResult.getStepType().equals("start")) {
            processLog.setStatus(1);
        } else {
            processLog.setStatus(-1);
        }
        this.save(processLog);

        if (ToolUtil.isNotEmpty(activitiStepsResult.getConditionNodeList()) && activitiStepsResult.getConditionNodeList().size() > 0) {
            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
                loopAdd(stepsResult, taskId);
            }
        }
        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildNode())) {
//            for (ActivitiStepsResult stepsResult : activitiStepsResult.getConditionNodeList()) {
            loopAdd(activitiStepsResult.getChildNode(), taskId);
//            }
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

    /**
     * 查询下一级配置
     *
     * @param id
     * @return
     */
    public ChildAudit backChildAudit(Long id) {
        ActivitiAudit audit = auditService.query().eq("setps_id", id).one();
        if (ToolUtil.isNotEmpty(audit)) {
            ChildAudit childAudit = new ChildAudit();
            switch (audit.getType()) {
                case "person":
                case "supervisor":
                case "start":

                    childAudit.setType(audit.getType());
                    childAudit.setAuditRule(audit.getRule());
                    return childAudit;

                case "optional":
                case "performTask":
                case "completeTask":

                    childAudit.setType(audit.getType());
                    childAudit.setAuditRule(null);
                    return childAudit;
            }
        }
        return null;
    }
}

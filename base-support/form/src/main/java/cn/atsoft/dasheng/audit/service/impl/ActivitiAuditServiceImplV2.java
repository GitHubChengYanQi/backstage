package cn.atsoft.dasheng.audit.service.impl;


import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.audit.entity.ActivitiAuditV2;
import cn.atsoft.dasheng.audit.mapper.ActivitiAuditMapper;
import cn.atsoft.dasheng.audit.mapper.ActivitiAuditMapperV2;
import cn.atsoft.dasheng.audit.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResultV2;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.audit.service.ActivitiAuditServiceV2;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ActionStatus;
import cn.atsoft.dasheng.form.pojo.AppointUser;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.DeptPosition;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;

/**
 * <p>
 * 审批配置表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiAuditServiceImplV2 extends ServiceImpl<ActivitiAuditMapperV2, ActivitiAuditV2> implements ActivitiAuditServiceV2 {

//    @Autowired
//    private DocumentStatusService documentStatusService;
//    @Autowired
//    private ActivitiStepsService stepsService;
//    @Autowired
//    private ActivitiProcessFormLogService processLogService;
//    @Autowired
//    private ActivitiAuditService auditService;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ActivitiStepsService activitiStepsService;
//
//    @Autowired
//    private ActivitiProcessTaskService taskService;
//
//
//    @Override
//    @Transactional
//    public void add(ActivitiAuditParam param) {
//
//        ActivitiAudit entity = getEntity(param);
//        this.save(entity);
//
//    }
//
//    @Override
//    public List<ActivitiAuditV2> getListBySteps(List<ActivitiSteps> steps) {
//        List<Long> stepsId = new ArrayList<>();
//        for (ActivitiSteps step : steps) {
//            stepsId.add(step.getSetpsId());
//        }
//        return this.list(new QueryWrapper<ActivitiAuditV2>() {{
//            in("setps_id", stepsId);
//        }});
//    }
//
//    @Override
//    public List<ActivitiAudit> getListByStepsId(List<Long> stepsIds) {
//        if (ToolUtil.isEmpty(stepsIds)) {
//            return new ArrayList<>();
//        }
//        return this.list(new QueryWrapper<ActivitiAudit>() {{
//            in("setps_id", stepsIds);
//        }});
//    }
//
//    @Override
//    public void delete(ActivitiAuditParam param) {
//        param.setDisplay(0);
//        this.updateById(this.getEntity(param));
//    }
//
//    @Override
//    public ActivitiAuditResult getAudit(Long id) {
//        ActivitiAudit audit = this.getOne(new QueryWrapper<ActivitiAudit>() {{
//            eq("setps_id", id);
//        }});
//        ActivitiAuditResult activitiAuditResult = new ActivitiAuditResult();
//        ToolUtil.copyProperties(audit, activitiAuditResult);
//        return activitiAuditResult;
//    }
//
//    @Override
//    public void update(ActivitiAuditParam param) {
//        ActivitiAudit oldEntity = getOldEntity(param);
//        ActivitiAudit newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
//    }
//
//    @Override
//    public ActivitiAuditResult findBySpec(ActivitiAuditParam param) {
//        return null;
//    }
//
//    @Override
//    public List<ActivitiAuditResult> findListBySpec(ActivitiAuditParam param) {
//        return null;
//    }
//
//    @Override
//    public PageInfo findPageBySpec(ActivitiAuditParam param) {
//        Page<ActivitiAuditResult> pageContext = getPageContext();
//        IPage<ActivitiAuditResult> page = this.baseMapper.customPageList(pageContext, param);
//        return PageFactory.createPageInfo(page);
//    }
//
//    @Override
//    public List<ActivitiAuditResult> backAudits(List<Long> ids) {
//        List<ActivitiAuditResult> auditResults = new ArrayList<>();
//        if (ids.size() > 0) {
//            List<ActivitiAudit> audits = this.list(new QueryWrapper<ActivitiAudit>() {{
//                in("setps_id", ids);
//            }});
//
//            for (ActivitiAudit audit : audits) {
//                ActivitiAuditResult auditResult = new ActivitiAuditResult();
//                ToolUtil.copyProperties(audit, auditResult);
//                DocumentsStatusResult detail = documentStatusService.detail(auditResult.getDocumentsStatusId());
//                auditResult.setStatusResult(detail);
//                if (ToolUtil.isNotEmpty(auditResult.getAction())) {
//                    List<ActionStatus> statuses = JSON.parseArray(auditResult.getAction(), ActionStatus.class);
//                    auditResult.setStatuses(statuses);
//                }
//                auditResults.add(auditResult);
//            }
//        }
//        return auditResults;
//    }
    @Override
    public List<ActivitiAuditResultV2> resultByIds(List<Long> ids) {
        return BeanUtil.copyToList(this.lambdaQuery().in(ActivitiAuditV2::getSetpsId,ids).list(),ActivitiAuditResultV2.class);
    }
//
//    /**
//     * 审批流程可执行人
//     *
//     * @param taskId
//     * @return
//     */
//    @Override
//    public List<Long> getUserIds(Long taskId) {
//        List<Long> userIds = new ArrayList<>();
//        Long userId = LoginContextHolder.getContext().getUserId();
//        List<Long> depts = LoginContextHolder.getContext().getDeptDataScope();
//        List<ActivitiProcessLog> processLogs = ToolUtil.isEmpty(taskId) ? new ArrayList<>() : processLogService.query().eq("task_id", taskId).list();
//        List<Long> stepIds = new ArrayList<>();
//        for (ActivitiProcessLog processLog : processLogs) {
//            stepIds.add(processLog.getSetpsId());
//        }
//        List<ActivitiAudit> audits = stepIds.size() == 0 ? new ArrayList<>() : auditService.query().in("setps_id", stepIds).list();
//        for (ActivitiAudit activitiAudit : audits) {
//            AuditRule rule = activitiAudit.getRule();
//
//            for (AuditRule.Rule ruleRule : rule.getRules()) {
//
//                switch (ruleRule.getType()) {
//                    case AppointUsers:
//                        for (AppointUser appointUser : ruleRule.getAppointUsers()) {
//                            userIds.add(Long.valueOf(appointUser.getKey()));
//                        }
//                        break;
//                    case AllPeople:
//                        List<Long> allUsersId = userService.getAllUsersId();
//                        userIds.addAll(allUsersId);
//                        break;
//                    case DeptPositions:
//                        for (DeptPosition deptPosition : ruleRule.getDeptPositions()) {
//                            List<Long> positionIds = new ArrayList<>();
//                            for (DeptPosition.Position position : deptPosition.getPositions()) {
//                                if (ToolUtil.isNotEmpty(position.getValue())) {
//                                    positionIds.add(Long.valueOf(position.getValue()));
//                                }
//                            }
//                            List<User> userByPositionAndDept = userService.getUserByPositionAndDept(Long.valueOf(deptPosition.getKey()), positionIds);
//                            for (User user : userByPositionAndDept) {
//                                userIds.add(user.getUserId());
//                            }
//                        }
//                        break;
//                    case MasterDocumentPromoter:    //主单据发起人
//                        if (ToolUtil.isNotEmpty(taskId)) {
//                            ActivitiProcessTask processTask = taskService.getById(taskId);
//                            if (ToolUtil.isNotEmpty(processTask.getMainTaskId())) {
//                                ActivitiProcessTask mainTask = taskService.getById(processTask.getMainTaskId());
//                                userIds.add(mainTask.getCreateUser());
//                            }
//                        }
//                }
//
//            }
//
//        }
//        if (ToolUtil.isNotEmpty(taskId)) {
//            ActivitiProcessTask processTask = taskService.getById(taskId);
//            if (ToolUtil.isNotEmpty(processTask) && ToolUtil.isNotEmpty(processTask.getUserIds())) {
//                userIds.addAll(JSON.parseArray(processTask.getUserIds(), Long.class));
//            }
//        }
//
//
//        return userIds;
//    }
//
//    private Serializable getKey(ActivitiAuditParam param) {
//        return param.getAuditId();
//    }
//
//    private Page<ActivitiAuditResult> getPageContext() {
//        return PageFactory.defaultPage();
//    }
//
//    private ActivitiAudit getOldEntity(ActivitiAuditParam param) {
//        return this.getById(getKey(param));
//    }
//
//    private ActivitiAudit getEntity(ActivitiAuditParam param) {
//        ActivitiAudit entity = new ActivitiAudit();
//        ToolUtil.copyProperties(param, entity);
//        return entity;
//    }
//
//    @Override
//    public void power(ActivitiProcess activitiProcess) {
//        ActivitiSteps startSteps = activitiStepsService.query().eq("process_id", activitiProcess.getProcessId()).eq("type", START).one();
//        if (ToolUtil.isNotEmpty(startSteps)) {
//            ActivitiAudit audit = this.query().eq("setps_id", startSteps.getSetpsId()).one();
//            if (!activitiStepsService.checkUser(audit.getRule())) {
//                throw new ServiceException(500, "您没有权限创建任务");
//            }
//        }
//    }
//
//    public Boolean checkUser(AuditRule starUser) {
//        LoginUser user = LoginContextHolder.getContext().getUser();
//        Long userId = user.getId();
//        List<Long> users = this.selectUsers(starUser);
//        for (Long aLong : users) {
//            if (aLong.equals(userId)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public List<Long> selectUsers(AuditRule starUser) {
//        List<Long> users = new ArrayList<>();
//
//        for (AuditRule.Rule rule : starUser.getRules()) {
//            switch (rule.getType()) {
//                case AppointUsers:
//                    for (AppointUser appointUser : rule.getAppointUsers()) {
//                        users.add(Long.valueOf(appointUser.getKey()));
//                    }
//                    break;
//                case AllPeople:
//                    List<Long> allUsersId = userService.getAllUsersId();
//                    users.addAll(allUsersId);
//                    break;
//                case DeptPositions:
//                    for (DeptPosition deptPosition : rule.getDeptPositions()) {
//                        List<Long> positionIds = new ArrayList<>();
//                        for (DeptPosition.Position position : deptPosition.getPositions()) {
//                            if (ToolUtil.isNotEmpty(position.getValue())) {
//                                positionIds.add(Long.valueOf(position.getValue()));
//                            }
//                        }
//                        List<User> userByPositionAndDept = userService.getUserByPositionAndDept(Long.valueOf(deptPosition.getKey()), positionIds);
//                        for (User user : userByPositionAndDept) {
//                            users.add(user.getUserId());
//                        }
//                    }
//
//                    break;
//            }
//        }
//
//        return users;
//    }


}

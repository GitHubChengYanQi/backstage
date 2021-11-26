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
import cn.atsoft.dasheng.form.pojo.ChildAudit;
import cn.atsoft.dasheng.form.pojo.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
    private ActivitiProcessTaskServiceImpl activitiProcessTaskService;
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


    @Transactional
    @Override
    public void add(ActivitiProcessLogParam param) {
        ActivitiProcessLog entity = getEntity(param);
        List<Long> users = new ArrayList<>();
//        boolean flag2 = users.contains(LoginContextHolder.getContext().getUserId());
//        int admin = activitiProcessTaskService.isAdmin(param.getTaskId());
//        if (admin == 0 && flag2 == false) {
//            throw new ServiceException(500, "抱歉，您没有权限进行删除");
//        } else {
//          }
        ActivitiSteps steps = stepsService.getById(param.getSetpsId());
        ActivitiProcess process = processService.getById(steps.getProcessId());

        ActivitiAudit audit = auditService.query().eq("setps_id", steps.getChildren()).one();
        ActivitiAudit nowAudit = auditService.query().eq("setps_id", steps.getSetpsId()).one();
        if (audit.getType().equals("person")) {
            LoginUser loginUser = LoginContextHolder.getContext().getUser();
            Boolean userFlag = false;
            Boolean deptFlag = false;
            if (ToolUtil.isNotEmpty(nowAudit.getRule()) && ToolUtil.isNotEmpty(nowAudit.getRule().getStartUsers()) && ToolUtil.isNotEmpty(nowAudit.getRule().getStartUsers().getUsers())) {
                for (StartUsers.Users user : nowAudit.getRule().getStartUsers().getUsers()) {
                    if (user.getKey().equals(loginUser.getId().toString())) {
                        userFlag = true;
                    }
                }
            } else if (ToolUtil.isNotEmpty(nowAudit.getRule()) && ToolUtil.isNotEmpty(nowAudit.getRule().getStartUsers().getDepts()) && ToolUtil.isNotEmpty(nowAudit.getRule().getStartUsers())) {
                for (StartUsers.Depts dept : nowAudit.getRule().getStartUsers().getDepts()) {
                    if (dept.getKey().equals(loginUser.getDeptId().toString())) {
                        deptFlag = true;
                    }
                }
            }
            if (!userFlag && !deptFlag) {
                throw new ServiceException(500, "您没有权限操作审批");
            }
        }



        ActivitiProcessTask activitiProcessTask = activitiProcessTaskService.query().eq("form_id", param.getFormId()).one();
        entity.setSetpsId(steps.getSetpsId());
        entity.setPeocessId(activitiProcessTask.getProcessId());
        entity.setTaskId(activitiProcessTask.getProcessTaskId());
        this.save(entity);

        if (entity.getStatus() == 1) {
            taskSend.logAddSend(audit.getType(), audit.getRule(), process.getUrl(), steps.getChildren(), activitiProcessTask.getFormId());
            if (audit.getType().equals("send")) {
                ActivitiSteps children = stepsService.query().eq("children", steps.getChildren()).one();
                ActivitiAudit nextAudit = auditService.query().eq("setps_id", children.getChildren()).one();
                taskSend.send(nextAudit.getType(), nextAudit.getRule(), process.getUrl(), children.getChildren(), param.getFormId());
            }
        } else {

            List<QualityTask> qualityTasks = qualityTaskService.query().eq("quality_task_id", activitiProcessTask.getFormId()).list();
            if (qualityTasks.size() > 0) {
                taskSend.vetoSend(audit.getType(), process.getUrl(), steps.getChildren(), activitiProcessTask.getFormId());
            }
        }
//
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

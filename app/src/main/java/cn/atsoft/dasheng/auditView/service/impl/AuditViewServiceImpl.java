package cn.atsoft.dasheng.auditView.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.auditView.entity.AuditView;
import cn.atsoft.dasheng.auditView.mapper.AuditViewMapper;
import cn.atsoft.dasheng.auditView.model.params.AuditViewParam;
import cn.atsoft.dasheng.auditView.model.result.AuditViewResult;
import cn.atsoft.dasheng.auditView.service.AuditViewService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
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
 * 所有审核 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
@Service
public class AuditViewServiceImpl extends ServiceImpl<AuditViewMapper, AuditView> implements AuditViewService {
    @Autowired
    private ActivitiProcessLogService logService;
    @Autowired
    private ActivitiProcessTaskService taskService;


    @Override
    public void add(AuditViewParam param) {
        AuditView entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AuditViewParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AuditViewParam param) {
        AuditView oldEntity = getOldEntity(param);
        AuditView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AuditViewResult findBySpec(AuditViewParam param) {
        return null;
    }

    @Override
    public List<AuditViewResult> findListBySpec(AuditViewParam param) {
        return null;
    }

    @Override
    public PageInfo<AuditViewResult> findPageBySpec(AuditViewParam param) {
        Page<AuditViewResult> pageContext = getPageContext();
        IPage<AuditViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AuditViewParam param) {
        return param.getAuditViewId();
    }

    private Page<AuditViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AuditView getOldEntity(AuditViewParam param) {
        return this.getById(getKey(param));
    }

    private AuditView getEntity(AuditViewParam param) {
        AuditView entity = new AuditView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void addView(Long taskId) {
        ActivitiProcessTask task = taskService.getById(taskId);
        List<ActivitiProcessLogResult> logAudit = logService.getLogAudit(taskId);

        List<AuditView> auditViews = new ArrayList<>();

//        LoginUser loginUser = LoginContextHolder.getContext().getUser();

        for (ActivitiProcessLogResult logResult : logAudit) {
            AuditRule rule = logResult.getActivitiAudit().getRule();
            if (logResult.getActivitiAudit().getType().equals("process")) {
                if (rule.getType().toString().equals("audit")) {
                    AuditView auditView = new AuditView();
                    auditView.setTaskType(task.getType());
//                    auditView.setUserId(loginUser.getId());
                    auditView.setProcessId(logResult.getPeocessId());
                    auditView.setAuditLogId(logResult.getLogId());
                    auditView.setProcessId(logResult.getPeocessId());
                    auditViews.add(auditView);
                }
            }
        }
        this.saveBatch(auditViews);
    }
    @Override
    public void microAddView(Long taskId,Long userId) {
        ActivitiProcessTask task = taskService.getById(taskId);
        List<ActivitiProcessLogResult> logAudit = logService.getLogAudit(taskId);

        List<AuditView> auditViews = new ArrayList<>();

//        LoginUser loginUser = LoginContextHolder.getContext().getUser();

        for (ActivitiProcessLogResult logResult : logAudit) {
            AuditRule rule = logResult.getActivitiAudit().getRule();
            if (logResult.getActivitiAudit().getType().equals("process")) {
                if (rule.getType().toString().equals("audit")) {
                    AuditView auditView = new AuditView();
                    auditView.setTaskType(task.getType());
                    auditView.setUserId(userId);
                    auditView.setProcessId(logResult.getPeocessId());
                    auditView.setAuditLogId(logResult.getLogId());
                    auditView.setProcessId(logResult.getPeocessId());
                    auditViews.add(auditView);
                }
            }
        }
        this.saveBatch(auditViews);
    }



}

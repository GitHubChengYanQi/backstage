package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.impl.QualityTaskServiceImpl;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessLogMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.hutool.json.JSONUtil;
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
    private ActivitiProcessTaskServiceImpl activitiProcessTaskService;
    @Autowired
    private ActivitiAuditService auditService;
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private QualityTaskServiceImpl qualityTaskService;

    @Override
    public void add(ActivitiProcessLogParam param) {
        ActivitiProcessLog entity = getEntity(param);
        List<Long> users = new ArrayList<>();
        boolean flag2 = users.contains(LoginContextHolder.getContext().getUserId());
        int admin = activitiProcessTaskService.isAdmin(param.getTaskId());
        if (admin == 0 && flag2 == false) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        } else {
            this.save(entity);
            ActivitiSteps stepsNext = stepsService.query().eq("supper", param.getSetpsId()).one();
            ActivitiProcess process = processService.getById(stepsNext.getProcessId());
            ActivitiAudit audit = auditService.query().eq("setps_id",stepsNext.getSetpsId()).one();

            WxCpTemplate wxCpTemplate = new WxCpTemplate();
            List<Long> userIds = new ArrayList<>();
            switch (audit.getType()){
                case "指定人":
                    StartUsers bean = JSONUtil.toBean(audit.getRule(), StartUsers.class);
                    users.add(bean.getValue());
                    wxCpTemplate.setUserIds(userIds);
                    String url = process.getUrl().replace("setpsId",stepsNext.getSetpsId().toString());
                    wxCpTemplate.setUrl(url);
                    wxCpTemplate.setTitle("您有新的待审批任务");
                    wxCpTemplate.setDescription("您有新的待审批任务");
                    wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                    wxCpSendTemplate.sendTemplate();
                    break;
                case "执行任务":
                    QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", param.getTaskId()).one();

                    userIds.add(qualityTask.getUserId());
                    wxCpTemplate.setTitle("您有新的待执行任务");
                    wxCpTemplate.setDescription("您有新的待执行任务");
                    wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                    wxCpSendTemplate.sendTemplate();
            }
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

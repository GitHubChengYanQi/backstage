package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.impl.QualityTaskServiceImpl;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessTaskMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 流程任务表	 服务实现类
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
@Service
public class ActivitiProcessTaskServiceImpl extends ServiceImpl<ActivitiProcessTaskMapper, ActivitiProcessTask> implements ActivitiProcessTaskService {
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private QualityTaskServiceImpl qualityTaskService;
    @Override
    public void add(ActivitiProcessTaskParam param) {
        ActivitiProcessTask entity = getEntity(param);
        this.save(entity);
        ActivitiProcess process = processService.query().eq("process_id", param.getProcessId()).one();
        ActivitiSteps steps = stepsService.query().eq("process_id", param.getProcessId()).eq("type", 0).eq("supper",0).one();
        ActivitiSteps stepsNext = stepsService.query().eq("process_id", param.getProcessId()).eq("type", 1).eq("supper",steps.getSetpsId()).one();
        ActivitiAudit audit = auditService.query().eq("setps_id", stepsNext.getSetpsId()).one();


        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();

        switch (audit.getType()){
            case "指定人":
                StartUsers bean = JSONUtil.toBean(audit.getRule(), StartUsers.class);
                users.add(bean.getValue());
                wxCpTemplate.setUserIds(users);

//                String url = param.getUrl().replace("codeId", aLong.toString());
                String url = process.getUrl().replace("setpsValue",stepsNext.getSetpsId().toString());
                String formValue = url.replace("formValue", param.getQTaskId().toString());
                wxCpTemplate.setUrl(formValue);
                wxCpTemplate.setTitle("您有新的待审批任务");
                wxCpTemplate.setDescription("您有新的待审批任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
                break;
            case "执行任务":
                QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", param.getFormId()).one();
                users.add(qualityTask.getUserId());
                String url1 = param.getUrl();
                wxCpTemplate.setUrl(url1);

                wxCpTemplate.setTitle("您有新的待执行任务");
                wxCpTemplate.setDescription("您有新的待执行任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
        }
    }

    @Override
    public void delete(ActivitiProcessTaskParam param) {
        int admin = this.isAdmin(param.getProcessTaskId());
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行删除");
        }
        ActivitiProcessTask newEntity = getEntity(param);
        newEntity.setDisplay(0);
        this.updateById(newEntity);
    }

    @Override
    public void update(ActivitiProcessTaskParam param) {
        int admin = this.isAdmin(param.getProcessTaskId());
        ActivitiProcessTask oldEntity = getOldEntity(param);
        ActivitiProcessTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (admin == 0) {
            throw new ServiceException(500, "抱歉，您没有权限进行修改");
        }
        this.updateById(newEntity);
    }
    @Override
    public int isAdmin(Long taskId) {
        Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
        LoginContextHolder.getContext().getUserId();
        ActivitiProcessTask byId = this.getById(taskId);
        List<String> deptList = ToolUtil.isEmpty(byId.getDeptIds()) ? new ArrayList<>() : Arrays.asList(byId.getDeptIds().split(","));
        List<String> userList = ToolUtil.isEmpty(byId.getUserIds()) ? new ArrayList<>() : Arrays.asList(byId.getUserIds().split(","));
        boolean flag1 = deptList.contains(deptId.toString());
        boolean flag2 = userList.contains(deptId.toString());
        int flag = 0;
        if (LoginContextHolder.getContext().isAdmin()) {
            flag = 1;
        } else if (flag1 == false || flag2 == false) {
            flag = 0;
        } else {
            flag = 1;
        }
        return flag;
    }

    @Override
    public ActivitiProcessTaskResult findBySpec(ActivitiProcessTaskParam param) {
        return null;
    }

    @Override
    public List<ActivitiProcessTaskResult> findListBySpec(ActivitiProcessTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiProcessTaskResult> findPageBySpec(ActivitiProcessTaskParam param) {
        Page<ActivitiProcessTaskResult> pageContext = getPageContext();
        IPage<ActivitiProcessTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiProcessTaskParam param) {
        return param.getProcessTaskId();
    }

    private Page<ActivitiProcessTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiProcessTask getOldEntity(ActivitiProcessTaskParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcessTask getEntity(ActivitiProcessTaskParam param) {
        ActivitiProcessTask entity = new ActivitiProcessTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

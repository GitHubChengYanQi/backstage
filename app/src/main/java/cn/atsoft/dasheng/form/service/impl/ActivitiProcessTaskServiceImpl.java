package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.erp.service.impl.QualityTaskServiceImpl;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessTaskMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
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
    @Autowired
    private ActivitiProcessTaskSend taskSend;

    @Override
    public Long add(ActivitiProcessTaskParam param) {
        ActivitiProcessTask entity = getEntity(param);
        this.save(entity);
        ActivitiProcess process = processService.query().eq("process_id", param.getProcessId()).one();
        ActivitiSteps steps = stepsService.query().eq("process_id", param.getProcessId()).eq("type", 0).eq("supper", 0).one();
        ActivitiAudit audit = auditService.query().eq("setps_id", steps.getChildren()).one();
        taskSend.send(audit.getType(), audit.getRule(), process.getUrl(), steps.getChildren(), entity.getProcessTaskId());
        if (audit.getType().equals("send")) {
            ActivitiSteps children = stepsService.query().eq("children", steps.getChildren()).one();
            ActivitiAudit nextAudit = auditService.query().eq("setps_id", children.getChildren()).one();
            taskSend.send(nextAudit.getType(), nextAudit.getRule(), process.getUrl(), children.getChildren(), entity.getProcessTaskId());
        }
        return entity.getProcessTaskId();

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

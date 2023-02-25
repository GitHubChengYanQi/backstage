package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.entity.TaskParticipant;
import cn.atsoft.dasheng.form.mapper.TaskParticipantMapper;
import cn.atsoft.dasheng.form.model.params.TaskParticipantParam;
import cn.atsoft.dasheng.form.model.result.TaskParticipantResult;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.TaskParticipantService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-08-16
 */
@Service
public class TaskParticipantServiceImpl extends ServiceImpl<TaskParticipantMapper, TaskParticipant> implements TaskParticipantService {
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessTaskService taskService;


    @Override
    public void add(TaskParticipantParam param) {
        TaskParticipant entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 任务参与人添加
     *
     * @param taskId
     * @param userIds
     */
    @Override
    public void addTaskPerson(Long taskId, List<Long> userIds) {

        List<TaskParticipant> list = new ArrayList<>();
        for (Long userId : userIds) {
            TaskParticipant entity = new TaskParticipant();
            entity.setProcessTaskId(taskId);
            entity.setUserId(userId);
            entity.setType("order");
            list.add(entity);
        }
        this.saveBatch(list);
    }


    @Override
    public void delete(TaskParticipantParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(TaskParticipantParam param) {
        TaskParticipant oldEntity = getOldEntity(param);
        TaskParticipant newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 判断当前流程是否有 主单据发起人
     *
     * @param processId
     */
    @Override
    public Boolean MasterDocumentPromoter(Long processId) {
        List<ActivitiSteps> activitiSteps = stepsService.query().eq("process_id", processId).eq("display", 1).list();

        List<Long> stepIds = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            stepIds.add(activitiStep.getSetpsId());
        }
        if (ToolUtil.isNotEmpty(stepIds)) {
            Integer count = auditService.query().in("setps_id", stepIds).like("rule", "MasterDocumentPromoter").count();
            if (count > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public TaskParticipantResult findBySpec(TaskParticipantParam param) {
        return null;
    }

    @Override
    public List<TaskParticipantResult> findListBySpec(TaskParticipantParam param) {
        return null;
    }

    @Override
    public PageInfo<TaskParticipantResult> findPageBySpec(TaskParticipantParam param) {
        Page<TaskParticipantResult> pageContext = getPageContext();
        IPage<TaskParticipantResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TaskParticipantParam param) {
        return param.getParticipantId();
    }

    private Page<TaskParticipantResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TaskParticipant getOldEntity(TaskParticipantParam param) {
        return this.getById(getKey(param));
    }

    private TaskParticipant getEntity(TaskParticipantParam param) {
        TaskParticipant entity = new TaskParticipant();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

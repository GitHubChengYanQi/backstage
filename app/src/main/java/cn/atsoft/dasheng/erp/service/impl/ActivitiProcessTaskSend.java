package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.ActivitiTaskSend;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class ActivitiProcessTaskSend {
    private static final String PERSON="quality_task_person";
    private static final String COMPLETE="quality_task_complete";
    private static final String SEND="quality_task_send";
    private static final String DISPATCH="quality_task_dispatch";
    private static final String PERFORM="quality_task_perform";
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private OrCodeBindService bindService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiStepsService activitiStepsService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private QualityTaskDetailService qualityTaskDetailService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    private Logger logger = LoggerFactory.getLogger(ActivitiProcessTaskSend.class);

    /**
     * 根据传入参数换取 节点里面包含人的条件规则 找到人
     * @param starUser
     * @return
     */
    public List<Long> selectUsers(AuditRule starUser) {
        List<Long> users = new ArrayList<>();
        if (ToolUtil.isNotEmpty(starUser.getQualityRules().getUsers())) {
            for (QualityRules.Users user : starUser.getQualityRules().getUsers()) {
                users.add(Long.valueOf(user.getKey()));
            }
        }
        if (starUser.getQualityRules().perform) {
            List<User> allUser = userService.list();
            for (User user : allUser) {
                users.add(user.getUserId());
            }
        }
        if (ToolUtil.isNotEmpty(starUser.getQualityRules().getDepts())) {
            Long deptIds = 0L;
            List<Long> positionIds = new ArrayList<>();
            for (QualityRules.Depts dept : starUser.getQualityRules().getDepts()) {
                deptIds=Long.valueOf(dept.getKey());
                for (QualityRules.Depts.Positions position : dept.getPositions()) {
                    positionIds.add(Long.valueOf(position.getValue()));
                }
            }
            List<User> userList = userService.getBaseMapper().listUserByPositionAndDept(positionIds,deptIds);
            for (User user : userList) {
                users.add(user.getUserId());
            }
        }
        return users;
    }

    /**
     * 推送方法
     * @param type
     * @param starUser
     * @param taskId
     * @param status
     */
    public void send(String type, AuditRule starUser, Long taskId,int status) {
        List<Long> users = new ArrayList<>();
        List<Long> collect = new ArrayList<>();
        ActivitiTaskSend activitiTaskSend = new ActivitiTaskSend();
        if (ToolUtil.isNotEmpty(starUser)) {
            users = this.selectUsers(starUser);
        }
        if (ToolUtil.isNotEmpty(users)) {
            collect = users.stream().distinct().collect(Collectors.toList());
        }
        activitiTaskSend.setUsers(collect);
        activitiTaskSend.setTaskId(taskId);

        switch (type) {
            case PERSON:
                if (status == 1){
                    this.personSend(activitiTaskSend);
                }else {
                    vetoSend(taskId);
                }
                break;
            case PERFORM:
//                this.performTask(taskId);
                break;
            case COMPLETE:
                this.completeTaskSend(taskId);
                break;
            case SEND:
                this.sendSend(activitiTaskSend);
                break;
            case DISPATCH:
                this.dispatch(activitiTaskSend);
                break;
        }
    }

    /**
     * 关于质检任务的map
     * @param taskId
     * @return
     */
    private Map<String, String> getAboutSend(Long taskId) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
        User byId = userService.getById(qualityTask.getCreateUser());
        Map<String, String> map = new HashMap<>();
//        map.put("qualityTaskUserId", qualityTask.getUserId().toString());
        map.put("url", qualityTask.getUrl());
        map.put("qualityTaskId", qualityTask.getQualityTaskId().toString());
        map.put("coding", qualityTask.getCoding());
        map.put("byIdName", byId.getName());
        return map;
    }

    /**
     * 开始质检任务推送
     * @param taskId
     */
    private void performTask(Long taskId) {
        Map<String, String> aboutSend = this.getAboutSend(taskId);
        List<Long> users = new ArrayList<>();
        users.add(Long.valueOf(aboutSend.get("qualityTaskUserId")));
        String url = mobileService.getMobileConfig().getUrl();
        url = url +"OrCode?id="+aboutSend.get("orcodeId").toString();
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setTitle("已被分派新的待执行任务");
        wxCpTemplate.setDescription(aboutSend.get("byIdName") + "已发起质检任务" + aboutSend.get("coding"));
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    /**
     * 审批节点推送
     * @param param
     */
    private void personSend(ActivitiTaskSend param) {
        Map<String, String> aboutSend = this.getAboutSend(param.getTaskId());
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUserIds(param.getUsers());
        String url = mobileService.getMobileConfig().getUrl();
        url = url + "Work/Workflow?" + "id=" + param.getTaskId().toString();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("您有新的审批流程");
        wxCpTemplate.setDescription(aboutSend.get("byIdName") + "发起了任务" + aboutSend.get("coding"));
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    /**
     * 分派任务节点推送
     * @param param
     */
    private void dispatch(ActivitiTaskSend param) {
        Map<String, String> aboutSend = this.getAboutSend(param.getTaskId());
        ActivitiProcessTask byId = activitiProcessTaskService.getById(param.getTaskId());
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUserIds(param.getUsers());
        String url = mobileService.getMobileConfig().getUrl();
        url = url + "Work/Workflow/DispatchTask?id=" + param.getTaskId().toString();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("分派新的执行任务任务");
        wxCpTemplate.setDescription(aboutSend.get("byIdName") + "发起的任务" + "已被分派到您" + aboutSend.get("coding"));
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    /**
     * 任务完完成节点推送
     * @param taskId
     */
    private void completeTaskSend(Long taskId) {
        Map<String, String> aboutSend = this.getAboutSend(taskId);
        List<Long> users = new ArrayList<>();
        QualityTask updateEntity = new QualityTask();
        updateEntity.setQualityTaskId(Long.valueOf(aboutSend.get("qualityTaskId")));
        updateEntity.setState(2);
        qualityTaskService.updateById(updateEntity);
        logger.info(updateEntity.toString());

//        List<QualityTaskDetail> list = qualityTaskDetailService.query().eq("quality_task_id", aboutSend.get("qualityTaskId")).list();
        List<QualityTask> qualityTaskList = qualityTaskService.query().eq("parent_id",aboutSend.get("qualityTaskId")).list();
        if (ToolUtil.isNotEmpty(qualityTaskList)) {
            for (QualityTask detail : qualityTaskList) {
                List<Long> userIds = Arrays.asList(detail.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

                detail.setState(2);

                List<Long> collect = userIds.stream().distinct().collect(Collectors.toList());
                String url = mobileService.getMobileConfig().getUrl();

                url = url + "Work/Quality?id=" + detail.getQualityTaskId().toString();
                WxCpTemplate wxCpTemplate = new WxCpTemplate();
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setUserIds(collect);
                wxCpTemplate.setTitle("质检任务完成，待批准入库");
                wxCpTemplate.setDescription(aboutSend.get("coding"));
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
            }
            qualityTaskService.updateBatchById(qualityTaskList);
        }

    }

    /**
     * 抄送人节点推送
     * @param param
     */
    private void sendSend(ActivitiTaskSend param) {
        Map<String, String> aboutSend = this.getAboutSend(param.getTaskId());

        String url = mobileService.getMobileConfig().getUrl();
        url = url + "Work/Workflow?" + "id=" + param.getTaskId().toString();
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(param.getUsers());
        wxCpTemplate.setTitle("抄送");
        wxCpTemplate.setDescription(aboutSend.get("byIdName") + "发起的任务" + "已被上一级批准" + aboutSend.get("coding"));
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
        activitiProcessLogService.audit(param.getTaskId(), 1);

    }

    /**
     * 审批节点拒绝 推送
     * @param taskId
     */
    public void vetoSend(Long taskId) {

        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
        users.add(task.getCreateUser());
        String url = mobileService.getMobileConfig().getUrl();
        url = url + "Work/Workflow?" + "id=" + taskId.toString();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setTitle("您发起的流程已被否决");
        wxCpTemplate.setDescription(qualityTask.getCoding() + "审批被否决");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();

    }
}

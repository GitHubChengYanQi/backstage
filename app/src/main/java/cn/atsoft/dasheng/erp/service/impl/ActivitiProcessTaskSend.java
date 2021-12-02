package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.ActivitiTaskSend;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
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

    public List<Long> selectUsers(AuditRule starUser) {
        List<Long> users = new ArrayList<>();
        if (ToolUtil.isNotEmpty(starUser.getQualityRules().getUsers())) {
            for (QualityRules.Users user : starUser.getQualityRules().getUsers()) {
                users.add(Long.valueOf(user.getKey()));
            }
        }
        if (ToolUtil.isNotEmpty(starUser.getQualityRules().getDepts())) {
            List<Long> deptIds = new ArrayList<>();
            List<Long> positionIds = new ArrayList<>();
            for (QualityRules.Depts dept : starUser.getQualityRules().getDepts()) {
                deptIds.add(Long.valueOf(dept.getKey()));
                for (QualityRules.Depts.Positions position : dept.getPositions()) {
                    positionIds.add(Long.valueOf(position.getValue()));
                }
            }
            List<User> userList = userService.getBaseMapper().listUserByPositionAndDept(deptIds, positionIds);
            for (User user : userList) {
                users.add(user.getUserId());
            }
        }
        return users;
    }

    public void send(String type, AuditRule starUser, Long taskId) {
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
            case "quality_task_person":
                this.personSend(activitiTaskSend);
                break;
            case "quality_task_complete":
                this.completeTaskSend(taskId);
                break;
            case "quality_task_send":
                this.sendSend(activitiTaskSend);
                break;
            case "quality_task_dispatch":
                this.dispatch(activitiTaskSend);
                break;
        }
    }


    private Map<String, String> getAboutSend(Long taskId) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
        OrCodeBind formId = bindService.query().eq("form_id", qualityTask.getQualityTaskId()).one();
        User byId = userService.getById(qualityTask.getCreateUser());
        Map<String, String> map = new HashMap<>();
//        map.put("qualityTaskUserId", qualityTask.getUserId().toString());
        map.put("url", qualityTask.getUrl());
        map.put("qualityTaskId", qualityTask.getQualityTaskId().toString());
        if (ToolUtil.isNotEmpty(formId)) {
            map.put("orcodeId", formId.getOrCodeId().toString());
        }
        map.put("coding", qualityTask.getCoding());
        map.put("byIdName", byId.getName());
        return map;
    }


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

    private void dispatch(ActivitiTaskSend param) {
        Map<String, String> aboutSend = this.getAboutSend(param.getTaskId());
        ActivitiProcessTask byId = activitiProcessTaskService.getById(param.getTaskId());
//        param.setTaskId(byId.getFormId());
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

//    private void performTask(Long taskId) {
//        Map<String, String> aboutSend = this.getAboutSend(taskId);
//        List<Long> users = new ArrayList<>();
//        users.add(Long.valueOf(aboutSend.get("qualityTaskUserId")));
//        String url = mobileService.getMobileConfig().getUrl();
//        url = url +"OrCode?id="+aboutSend.get("orcodeId").toString();
//        WxCpTemplate wxCpTemplate = new WxCpTemplate();
//        wxCpTemplate.setUrl(url);
//        wxCpTemplate.setUserIds(users);
//        wxCpTemplate.setTitle("您有新的待执行任务");
//        wxCpTemplate.setDescription(aboutSend.get("byIdName") + "已发起质检任务" + aboutSend.get("coding"));
//        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//        wxCpSendTemplate.sendTemplate();
//    }

    private void completeTaskSend(Long taskId) {
        Map<String, String> aboutSend = this.getAboutSend(taskId);
        List<Long> users = new ArrayList<>();
        QualityTask updateEntity = new QualityTask();
        updateEntity.setQualityTaskId(Long.valueOf(aboutSend.get("qualityTaskId")));
        updateEntity.setState(2);
        qualityTaskService.updateById(updateEntity);
        logger.info(updateEntity.toString());

        List<QualityTaskDetail> list = qualityTaskDetailService.query().eq("quality_task_id", aboutSend.get("qualityTaskId")).list();
        for (QualityTaskDetail detail : list) {
            List<Long> userIds = Arrays.asList(detail.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            users.addAll(userIds);
        }
        List<Long> collect = users.stream().distinct().collect(Collectors.toList());
        String url = mobileService.getMobileConfig().getUrl();
        url = url + "OrCode?id=" + aboutSend.get("orcodeId").toString();
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(collect);
        wxCpTemplate.setTitle("质检任务完成，待批准入库");
        wxCpTemplate.setDescription(aboutSend.get("coding"));
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

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
        activitiProcessLogService.add(param.getTaskId(), 1);

    }

    //        public void logAddSend(String type, AuditRule starUser, String url, String stepsId, Long taskId) {
//        WxCpTemplate wxCpTemplate = new WxCpTemplate();
//        List<Long> users = new ArrayList<>();
//        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
//        Long qualityTaskId = task.getFormId();
//        QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
//        OrCodeBind formId = bindService.query().eq("form_id", qualityTask.getQualityTaskId()).one();
//        User byId = userService.getById(qualityTask.getCreateUser());
//        switch (type) {
//            case "person":
//                users = new ArrayList<>();
//                if (ToolUtil.isNotEmpty(starUser.getQualityRules().getUsers())) {
//                    for (QualityRules.Users user : starUser.getQualityRules().getUsers()) {
//                        users.add(Long.valueOf(user.getKey()));
//                    }
//                }
//                if (ToolUtil.isNotEmpty(starUser.getQualityRules().getDepts())) {
//                    List<Long> deptIds = new ArrayList<>();
//                    for (QualityRules.Depts dept : starUser.getQualityRules().getDepts()) {
//                        deptIds.add(Long.valueOf(dept.getKey()));
//                    }
//                    List<User> userList = userService.query().in("dept_id", deptIds).eq("status", "ENABLE").list();
//                    for (User user : userList) {
//                        users.add(user.getUserId());
//                    }
//                }
//                wxCpTemplate.setUserIds(users);
//                String setpsValue = url.replace("setpsvalue", stepsId.toString());
//                String formValue = setpsValue.replace("formvalue", taskId.toString());
//                wxCpTemplate.setUrl(formValue);
//                wxCpTemplate.setTitle("您有新的待审批任务");
//                wxCpTemplate.setDescription(byId.getName()+"发起的任务"+"已被上一级批准"+qualityTask.getCoding());
//                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//                wxCpSendTemplate.sendTemplate();
//                break;
//            case "performTask":
//                users = new ArrayList<>();
//                users.add(qualityTask.getUserId());
//                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
//                wxCpTemplate.setUrl(url);
//                wxCpTemplate.setUserIds(users);
//                wxCpTemplate.setTitle("您有新的待执行任务");
//                wxCpTemplate.setDescription(byId.getName()+"发起的任务"+"已被上一级批准"+qualityTask.getCoding());
//                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//                wxCpSendTemplate.sendTemplate();
//                break;
//            case "completeTask":
//                users = new ArrayList<>();
//                QualityTask updateEntity = new QualityTask();
//                updateEntity.setQualityTaskId(qualityTask.getQualityTaskId());
//                updateEntity.setState(2);
//                qualityTaskService.updateById(updateEntity);
//
//                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
//                wxCpTemplate.setUrl(url);
//                wxCpTemplate.setUserIds(users);
//                wxCpTemplate.setTitle("质检任务完成，待批准入库");
//                wxCpTemplate.setDescription(qualityTask.getCoding());
//                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//                wxCpSendTemplate.sendTemplate();
//                break;
//            case "send":
//                users = new ArrayList<>();
//                for (QualityRules.Users user : starUser.getQualityRules().getUsers()) {
//                    users.add(Long.valueOf(user.getKey()));
//                }
//                String setpsValue1 = url.replace("setpsvalue", stepsId.toString());
//                String formValue1 = setpsValue1.replace("formvalue", taskId.toString());
//                wxCpTemplate.setUrl(formValue1);
//                wxCpTemplate.setUserIds(users);
//                wxCpTemplate.setTitle("抄送");
//                wxCpTemplate.setDescription(byId.getName()+"发起的任务"+"已被上一级批准"+qualityTask.getCoding());
//                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//                wxCpSendTemplate.sendTemplate();
//                break;
//        }
//    }
    public void vetoSend(String type, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();

        List<Long> users = new ArrayList<>();
        QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
        ActivitiProcessTask activitiProcessTask = activitiProcessTaskService.query().eq("form_id", qualityTaskId).one();
        users.add(activitiProcessTask.getCreateUser());
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setTitle("您发起的流程已被否决");
        wxCpTemplate.setDescription(qualityTask.getCoding() + "审批被否决");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();

    }
}

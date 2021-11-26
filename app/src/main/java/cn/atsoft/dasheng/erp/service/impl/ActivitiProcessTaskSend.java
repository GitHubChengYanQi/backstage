package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Data
public class ActivitiProcessTaskSend{
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
    public void send(String type, AuditRule starUser, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        switch (type){
            case "person":
                for (StartUsers.Users user : starUser.getStartUsers().getUsers()) {
                    users.add(Long.valueOf(user.getKey()));
                }

                wxCpTemplate.setUserIds(users);
                String setpsValue = url.replace("setpsvalue", stepsId.toString());
                String formValue = setpsValue.replace("formvalue", qualityTaskId.toString());
                wxCpTemplate.setUrl(formValue);
                wxCpTemplate.setTitle("您有新的待审批任务");
                wxCpTemplate.setDescription("您有新的待审批任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
                break;
            case "performtask":
                QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
                users.add(qualityTask.getUserId());
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setTitle("您有新的待执行任务");
                wxCpTemplate.setDescription("您有新的待执行任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
        }
    }

    public void logAddSend(String type, AuditRule starUser, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
        OrCodeBind formId = bindService.query().eq("form_id", qualityTask.getQualityTaskId()).one();
        switch (type) {
            case "person":
                users = new ArrayList<>();
                if (ToolUtil.isNotEmpty(starUser.getStartUsers().getUsers())) {
                    for (StartUsers.Users user : starUser.getStartUsers().getUsers()) {
                        users.add(Long.valueOf(user.getKey()));
                    }
                }
                if (ToolUtil.isNotEmpty(starUser.getStartUsers().getDepts())) {
                    List<Long> deptIds = new ArrayList<>();
                    for (StartUsers.Depts dept : starUser.getStartUsers().getDepts()) {
                        deptIds.add(Long.valueOf(dept.getKey()));
                    }
                    List<User> userList = userService.query().in("dept_id", deptIds).eq("status", "ENABLE").list();
                    for (User user : userList) {
                        users.add(user.getUserId());
                    }
                }
                wxCpTemplate.setUserIds(users);
                String setpsValue = url.replace("setpsvalue", stepsId.toString());
                String formValue = setpsValue.replace("formvalue", qualityTaskId.toString());
                wxCpTemplate.setUrl(formValue);
                wxCpTemplate.setTitle("您有新的待审批任务");
                wxCpTemplate.setDescription("您有新的待审批任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
                break;
            case "performTask":
                users = new ArrayList<>();
                users.add(qualityTask.getUserId());
                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setUserIds(users);
                wxCpTemplate.setTitle("您有新的待执行任务");
                wxCpTemplate.setDescription("您有新的待执行任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
                break;
            case "completeTask":
                users = new ArrayList<>();
                QualityTask updateEntity = new QualityTask();
                updateEntity.setQualityTaskId(qualityTask.getQualityTaskId());
                updateEntity.setState(2);
                qualityTaskService.updateById(updateEntity);

                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setUserIds(users);
                wxCpTemplate.setTitle("质检任务完成，待批准入库");
                wxCpTemplate.setDescription("质检任务完成，待批准入库");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
                break;
        }
    }
    public void vetoSend(String type, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
        ActivitiProcessTask activitiProcessTask = activitiProcessTaskService.query().eq("form_id", qualityTaskId).one();
        users.add(activitiProcessTask.getCreateUser());
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setTitle("您发起的流程已被否决");
        wxCpTemplate.setDescription(qualityTask.getCoding()+"审批被否决");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();

    }
}

package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.StartUsers;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
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

    public void send(String type, String starUser, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        switch (type){
            case "person":
                AuditRule bean = JSONUtil.toBean(starUser, AuditRule.class);
                for (StartUsers.Users user : bean.getStartUsers().getUsers()) {
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

    public void logAddSend(String type, String starUser, String url, String stepsId, Long qualityTaskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = new ArrayList<>();
        QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", qualityTaskId).one();
        users.add(qualityTask.getUserId());
        OrCodeBind formId = bindService.query().eq("form_id", qualityTask.getQualityTaskId()).one();
        switch (type) {
            case "person":
                AuditRule bean = JSONUtil.toBean(starUser, AuditRule.class);
                for (StartUsers.Users user : bean.getStartUsers().getUsers()) {
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
            case "performTask":

                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setUserIds(users);
                wxCpTemplate.setTitle("您有新的待执行任务");
                wxCpTemplate.setDescription("您有新的待执行任务");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
            case "completeTask":

                QualityTask updateEntity = new QualityTask();
                qualityTask.setQualityTaskId(qualityTask.getQualityTaskId());
                qualityTask.setState(2);
                qualityTaskService.updateById(updateEntity);

                url = qualityTask.getUrl().replace("codeId", formId.getOrCodeId().toString());
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setUserIds(users);
                wxCpTemplate.setTitle("质检任务完成，待批准入库");
                wxCpTemplate.setDescription("质检任务完成，待批准入库");
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
        }
    }
}

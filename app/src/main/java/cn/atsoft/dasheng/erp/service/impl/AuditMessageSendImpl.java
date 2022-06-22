package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.AuditMessageSend;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditMessageSendImpl implements AuditMessageSend {
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Override
    public void send(Long taskId, RuleType type, List<Long> users, String url, String createName) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        switch (type) {
            case audit:
                wxCpTemplate.setTitle("待审批");
                break;
            case send:
                wxCpTemplate.setTitle("抄送");
                break;

        }
        wxCpTemplate.setDescription(processTask.getTaskName());
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setUrl(url);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }
    public void refuse(Long taskId, RuleType type, List<Long> users, String url, String createName) {

    }

    public void statusSend(Long taskId, RuleType type, List<Long> users, String url, String createName) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setTitle("您有新的单据需要操作");
        wxCpTemplate.setDescription(processTask.getTaskName());
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setUrl(url);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

}

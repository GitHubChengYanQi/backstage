package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.AuditMessageSend;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
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
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        String title = new String();
        switch (type) {
            case audit:
                title = "待审批";
                break;
            case send:
                title = "抄送";
                break;

        }
        String finalTitle = title;
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setItems(finalTitle);
            setCreateUserName(createName);
            setUrl(url);
            setType(0);
            setDescription(processTask.getTaskName());
            setUserIds(users);
        }});
    }

    public void refuse(Long taskId, RuleType type, List<Long> users, String url, String createName) {

    }

    public void statusSend(Long taskId, RuleType type, List<Long> users, String url, String createName) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setItems("您有新的单据需要操作");
            setCreateUser(processTask.getCreateUser());
            setUrl(url);
            setType(0);
            setDescription(processTask.getTaskName());
            setUserIds(users);
        }});
    }

}

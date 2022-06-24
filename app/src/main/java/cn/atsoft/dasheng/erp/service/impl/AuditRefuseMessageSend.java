package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.AuditMessageSend;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuditRefuseMessageSend implements AuditMessageSend {
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private UserService userService;

    @Override
    public void send(Long taskId, RuleType type, List<Long> users, String url, String createName) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();

        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(processTask.getFormId());

        User user = userService.getById(processTask.getCreateUser());
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setItems("审批被否决");
            setCreateUserName(user.getName());
            setUrl(url);
            setDescription(createName + "创建的任务" + qualityTask.getCoding());
            setUserIds(users);
        }});
    }
}

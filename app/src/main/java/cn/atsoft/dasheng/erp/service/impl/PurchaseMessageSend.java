package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.service.AuditMessageSend;
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
public class PurchaseMessageSend implements AuditMessageSend {
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Override
    public void send(Long taskId, RuleType type, List<Long> users, String url, String createName) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);

        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setType(1);
            setItems("采购审批完成");
            setDescription(processTask.getTaskName());
            setUrl(url);
            setUserIds(users);
        }});

    }
}

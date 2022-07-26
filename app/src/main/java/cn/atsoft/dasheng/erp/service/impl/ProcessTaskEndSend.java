package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessTaskEndSend {
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserService userService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;


    public void endSend(Long taskId) {
        List<Long> users = new ArrayList<>();
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        users.add(processTask.getCreateUser());
        String url = mobileService.getMobileConfig().getUrl() + "/#/Receipts/ReceiptsDetail?id=" + processTask.getProcessTaskId();
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setFunction(MarkDownTemplateTypeEnum.done);
            setItems("流程已结束");
            setUrl(url);
//            setDescription(processTask.getTaskName());
            setUserIds(users);
            setSource("processTask");
            setSourceId(taskId);
            setTaskId(taskId);
            setCreateTime(processTask.getCreateTime());
            setCreateUser(processTask.getCreateUser());
        }});
        activitiProcessTaskService.updateById(processTask);
    }
}

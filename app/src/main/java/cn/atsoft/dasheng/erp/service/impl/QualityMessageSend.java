package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.AuditMessageSend;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QualityMessageSend implements AuditMessageSend {

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private MobileService mobileService;
    @Override
    public void send(Long taskId,RuleType type, List<Long> users,String url,String createName) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(processTask.getFormId());
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        switch (type){
            case quality_complete:
                wxCpTemplate.setTitle("任务已完成,待入库");
                break;
            case quality_dispatch:
                wxCpTemplate.setTitle("待分派");
                break;
            case quality_perform:
                wxCpTemplate.setTitle("已分派待执行");
                break;
        }
        wxCpTemplate.setDescription("发出"+createName+"创建的任务"+qualityTask.getCoding());
        wxCpTemplate.setUserIds(users);
        wxCpTemplate.setUrl(url);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();

    }
    public void dispatchSend(Long parentId, QualityTaskParam qualityTaskParam){
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        String userIds = qualityTaskParam.getUserIds();
        List<Long> users = Arrays.asList(userIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        wxCpTemplate.setUserIds(users);
        String url = mobileService.getMobileConfig().getUrl();
        url = url + "/#/Work/Quality?id=" + parentId;
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setDescription("点击查看新质检任务");
        wxCpTemplate.setTitle("您被分派新的任务");
        wxCpTemplate.setType(1);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }
    

}

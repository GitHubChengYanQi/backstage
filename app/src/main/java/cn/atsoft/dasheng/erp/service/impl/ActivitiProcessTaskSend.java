package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.ActivitiTaskSend;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.AppointUser;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.DeptPosition;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.beetl.ext.fn.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.RuleType.quality_complete;
import static cn.atsoft.dasheng.form.pojo.RuleType.send;

@Service
@Data
public class ActivitiProcessTaskSend {
    private static final String PERSON = "quality_task_person";
    private static final String COMPLETE = "quality_task_complete";
    private static final String SEND = "quality_task_send";
    private static final String DISPATCH = "quality_task_dispatch";
    private static final String PERFORM = "quality_task_perform";
    private static final String PROCESS = "process";

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

    @Autowired
    private QualityMessageSend qualityMessageSend;
    @Autowired
    private AuditMessageSendImpl auditMessageSend;

    private Logger logger = LoggerFactory.getLogger(ActivitiProcessTaskSend.class);

    /**
     * 根据传入参数换取 节点里面包含人的条件规则 找到人
     *
     * @param starUser
     * @return
     */
    public List<Long> selectUsers(AuditRule starUser) {
        List<Long> users = new ArrayList<>();

        for (AuditRule.Rule rule : starUser.getRules()) {
            switch (rule.getType()) {
                case AppointUsers:
                    for (AppointUser appointUser : rule.getAppointUsers()) {
                        users.add(Long.valueOf(appointUser.getKey()));
                    }
                    break;
                case AllPeople:
                    List<Long> allUsersId = userService.getAllUsersId();
                    users.addAll(allUsersId);
                    break;
                case DeptPositions:
                    Map<String, List> map = new HashMap<>();
                    for (DeptPosition deptPosition : rule.getDeptPositions()) {
                        List<Long> positionIds = new ArrayList<>();
                        for (DeptPosition.Position position : deptPosition.getPositions()) {
                            if (ToolUtil.isNotEmpty(position.getValue())) {
                                positionIds.add(Long.valueOf(position.getValue()));
                            }
                        }
                        List<User> userByPositionAndDept = userService.getUserByPositionAndDept(Long.valueOf(deptPosition.getKey()), positionIds);
                        for (User user : userByPositionAndDept) {
                            users.add(user.getUserId());
                        }
                    }

                    break;
            }
        }

        return users;
    }

//    private List<DepstPositions>  getDeptData(AuditRule starUser) {
//
//
//    }

    /**
     * 推送方法
     *
     * @param type
     * @param auditRule
     * @param taskId
     * @param status
     */
    public void send(RuleType type, AuditRule auditRule, Long taskId, int status) {
        List<Long> users = new ArrayList<>();

        Map<String, String> aboutSend = this.getAboutSend(taskId, type);//获取任务关联
        String url = aboutSend.get("url");//进入switch 拼装不同阶段使用不同的url
        String createName = aboutSend.get("byIdName");
        if (ToolUtil.isNotEmpty(auditRule)) {
            users = this.selectUsers(auditRule);
            users = users.stream().distinct().collect(Collectors.toList());
        }

        switch (type) {
            case audit:
            case send:
                auditMessageSend.send(taskId, type, users, url, createName);
                break;
            case quality_complete:
                this.completeSend(type,aboutSend);
                break;
            case quality_perform:
            case quality_dispatch:
                qualityMessageSend.send(taskId, type, users, url, createName);
                break;
        }

    }

    private void completeSend(RuleType type,Map<String, String> aboutSend) {
        List<Long> users = new ArrayList<>();
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(Long.valueOf(aboutSend.get("taskId")));
        List<QualityTask> list = qualityTaskService.list(new QueryWrapper<QualityTask>() {{
            eq("parent_id", processTask.getFormId());
            ge("state", 1);
        }});
        for (QualityTask qualityTask : list) {
            if (ToolUtil.isNotEmpty(qualityTask.getUserIds())){
                users = Arrays.asList(qualityTask.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            }
            String url = mobileService.getMobileConfig().getUrl() + "/cp/#/Work/Quality?id=" + qualityTask.getQualityTaskId();
            qualityMessageSend.send(Long.valueOf(aboutSend.get("taskId")), type, users, url,aboutSend.get("byIdName"));
        }
    }

    /**
     * 审批拒绝更新
     *
     * @param taskId
     */
    public void refuseTask(Long taskId) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(processTask.getFormId());
        //否决更新质检任务状态
        qualityTask.setState(4);
        qualityTaskService.updateById(qualityTask);
        //创建消息推送 推送给发起人

        //找到发起质检任务的人
        User createUser = userService.getById(qualityTask.getCreateUser());
        //获取推送人
        List<Long> users = new ArrayList<>();
        users.add(createUser.getUserId());

        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setTitle("审批被否决");
        wxCpTemplate.setDescription(createUser.getName() + "创建的任务" + qualityTask.getCoding());
        wxCpTemplate.setUserIds(users);
        //获取url
        Map<String, String> aboutSend = this.getAboutSend(taskId, send);
        wxCpTemplate.setUrl(aboutSend.get("url"));

        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    /**
     * 关于质检任务的map
     *
     * @param taskId
     * @return
     */
    private Map<String, String> getAboutSend(Long taskId, RuleType type) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        QualityTask qualityTask = qualityTaskService.getById(task.getFormId());
        User byId = userService.getById(qualityTask.getCreateUser());
        Map<String, String> map = new HashMap<>();
        map.put("taskId", taskId.toString());
        map.put("qualityTaskId", qualityTask.getQualityTaskId().toString());
        map.put("coding", qualityTask.getCoding());
        map.put("byIdName", byId.getName());
        String url = this.changeUrl(type, map);//组装url
        map.put("url", url);
        return map;
    }

    private String changeUrl(RuleType type, Map<String, String> map) {
        String url = mobileService.getMobileConfig().getUrl();
        switch (type) {
            case audit:
            case send:
                url = url + "/cp/#/Work/Workflow?" + "id=" + map.get("taskId");
                break;
//                case quality_perform:
//                url = url + "/cp/#/OrCode?id=" + map.get("orcodeId");
//                break;
            //TODO complete节点推送子任务  
//            case quality_complete:
            case quality_dispatch:
                url = url + "/cp/#/Work/Quality?id=" + map.get("qualityTaskId");
                break;

        }
        return url;
    }
}

package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.pojo.AppointUser;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.DeptPosition;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogV1Service;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogV1Service;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private StepsService activitiStepsService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private QualityTaskDetailService qualityTaskDetailService;
    @Autowired
    private ActivitiProcessLogV1Service activitiProcessLogService;

    @Autowired
    private QualityMessageSend qualityMessageSend;
    @Autowired
    private AuditMessageSendImpl auditMessageSend;
    @Autowired
    PurchaseMessageSend purchaseMessageSend;
    @Autowired
    private PurchaseAskService purchaseAskService;
    @Autowired
    private ActivitiProcessTaskService processTaskService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private InstockOrderService instockOrderService;

    private Logger logger = LoggerFactory.getLogger(ActivitiProcessTaskSend.class);

    /**
     * 根据传入参数换取 节点里面包含人的条件规则 找到人
     *
     * @param starUser
     * @return
     */
    public List<Long> selectUsers(AuditRule starUser, Long taskId) {
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
                case MasterDocumentPromoter:    //主单据发起人
                    if (ToolUtil.isNotEmpty(taskId)) {
                        ActivitiProcessTask processTask = processTaskService.getById(taskId);
                        if (ToolUtil.isNotEmpty(processTask.getMainTaskId())) {
                            ActivitiProcessTask mainTask = processTaskService.getById(processTask.getMainTaskId());
                            if (ToolUtil.isNotEmpty(mainTask)) {
                                users.add(mainTask.getCreateUser());
                            }
                        }
                        if (processTask.getType().equals("INSTOCKERROR")) {
                            AnomalyOrder anomalyOrder = anomalyOrderService.getById(processTask.getFormId());
                            InstockOrder instockOrder = instockOrderService.getById(anomalyOrder);
                            users.add(instockOrder.getCreateUser());
                        }
                    }
                    break;
                case Director:
                    if (ToolUtil.isNotEmpty(taskId)) {
                        ActivitiProcessTask processTask = processTaskService.getById(taskId);
                        if (ToolUtil.isNotEmpty(processTask) && ToolUtil.isNotEmpty(processTask.getUserId())) {
                            users.add(processTask.getUserId());
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
     */
    public void send(RuleType type, AuditRule auditRule, Long taskId, Long loginUserId) {
        List<Long> users = new ArrayList<>();

        Map<String, String> aboutSend = this.getAboutSend(taskId, type);//获取任务关联
        String url = aboutSend.get("url");//进入switch 拼装不同阶段使用不同的url
        String createName = aboutSend.get("byIdName");
        if (ToolUtil.isNotEmpty(auditRule)) {
            users = this.selectUsers(auditRule, taskId);
            users = users.stream().distinct().collect(Collectors.toList());
        }

        if (ToolUtil.isNotEmpty(users)) {
            switch (type) {
                case audit:
                case send:
                    auditMessageSend.send(taskId, type, users, url, createName);
                    break;
                case quality_complete:
                    this.completeSend(type, aboutSend);
                    break;
                case quality_perform:
                case quality_dispatch:
                    qualityMessageSend.send(taskId, type, users, url, createName);
                    break;
                case purchase_complete:
                    purchaseAskService.complateAsk(taskId, loginUserId);
                    this.completeSend(type, aboutSend);
                    break;
                case status:
                    auditMessageSend.statusSend(taskId, type, users, url, createName);
                    break;
            }

        }
    }

    private void completeSend(RuleType type, Map<String, String> aboutSend) {
        List<Long> users = new ArrayList<>();
        String url = mobileService.getMobileConfig().getUrl();
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(Long.valueOf(aboutSend.get("taskId")));
        switch (type) {
            case quality_complete:
                List<QualityTask> list = qualityTaskService.list(new QueryWrapper<QualityTask>() {{
                    eq("parent_id", processTask.getFormId());
                    ge("state", 1);
                }});
                for (QualityTask qualityTask : list) {
                    if (ToolUtil.isNotEmpty(qualityTask.getUserIds()) && !qualityTask.getState().equals(-1)) {
                        users = Arrays.asList(qualityTask.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    }
                    url = mobileService.getMobileConfig().getUrl() + "/#/Work/Quality?id=" + qualityTask.getQualityTaskId();
                    qualityMessageSend.send(Long.valueOf(aboutSend.get("taskId")), type, users, url, aboutSend.get("byIdName"));
                }

                url = mobileService.getMobileConfig().getUrl() + "/#/Receipts/ReceiptsDetail?" + "id=" + processTask.getProcessTaskId();
                qualityMessageSend.send(processTask.getProcessTaskId(), type, users, url, aboutSend.get("byIdName"));
                break;
            case purchase_complete:
                users.add(processTask.getCreateUser());
                url = url + "/#/Receipts/ReceiptsDetail?" + "id=" + processTask.getProcessTaskId();
                purchaseMessageSend.send(processTask.getProcessTaskId(), type, users, url, aboutSend.get("byIdName"));
                break;
        }
    }


    /**
     * 审批拒绝更新
     *
     * @param taskId
     */
    public void refuseTask(Long taskId) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(taskId);
        //否决更新质检任务状态

        //找到发起质检任务的人
        User createUser = userService.getById(processTask.getCreateUser());
        //获取推送人
        List<Long> users = new ArrayList<>();
        users.add(createUser.getUserId());

        Map<String, String> aboutSend = this.getAboutSend(taskId, send);

        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setFunction(MarkDownTemplateTypeEnum.refuse);
            setType(1);
            setItems("审批被否决");
            setUrl(aboutSend.get("url"));
//            setDescription(processTask.getTaskName());
            setSource("processTask");
            setSourceId(processTask.getProcessTaskId());
            setCreateTime(processTask.getCreateTime());
            setCreateUser(processTask.getCreateUser());
            setTaskId(processTask.getProcessTaskId());
            setUserIds(users);
        }});
    }

    /**
     * 关于质检任务的map
     *
     * @param taskId
     * @return
     */
    private Map<String, String> getAboutSend(Long taskId, RuleType type) {
        ActivitiProcessTask task = activitiProcessTaskService.getById(taskId);
        User byId = userService.getById(task.getCreateUser());
        Map<String, String> map = new HashMap<>();
        map.put("taskId", taskId.toString());
        map.put("type", task.getType());
        map.put("formId", task.getFormId().toString());
//        map.put("coding", qualityTask.getCoding());
        map.put("byIdName", byId.getName());
        String url = this.changeUrl(type, map, task);//组装url
        map.put("url", url);
        return map;
    }

    private String changeUrl(RuleType type, Map<String, String> map, ActivitiProcessTask processTask) {
        String url = mobileService.getMobileConfig().getUrl();
        switch (type) {
            case audit:
            case send:
            case status:
                url = url + "/#/Receipts/ReceiptsDetail?" + "id=" + map.get("taskId");
                break;
//
//                url = url + "/#/Receipts/ReceiptsDetail?" + "type=" + map.get("type") + "&formId=" + map.get("formId");
//                break;
//                case quality_perform:
//                url = url + "/cp/#/OrCode?id=" + map.get("orcodeId");
//                break;

//            case quality_complete:
            case quality_dispatch:
                QualityTask qualityTask = qualityTaskService.getById(processTask.getFormId());
                url = url + "/#/Work/Quality?id=" + qualityTask.getQualityTaskId();
                break;

        }
        return url;
    }
}

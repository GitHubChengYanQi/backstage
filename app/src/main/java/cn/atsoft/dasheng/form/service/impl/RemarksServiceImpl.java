package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.service.ShopCartService;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.enums.RemarkEnum;
import cn.atsoft.dasheng.form.mapper.RemarksMapper;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import cn.atsoft.dasheng.form.pojo.AuditParam;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * log备注 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
@Service
public class RemarksServiceImpl extends ServiceImpl<RemarksMapper, Remarks> implements RemarksService {
    @Autowired
    private ActivitiProcessLogService logService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private StepsService appStepService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private StepsService stepsService;


    @Override
    public void add(Long logId, String note) {
        Remarks remarks = new Remarks();
        remarks.setLogId(logId);
        remarks.setContent(note);
        this.save(remarks);
    }

    @Override
    public void delete(RemarksParam param) {
        Remarks entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(RemarksParam param) {
        Remarks oldEntity = getOldEntity(param);
        Remarks newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemarksResult findBySpec(RemarksParam param) {
        return null;
    }

    @Override
    public List<RemarksResult> findListBySpec(RemarksParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(RemarksParam param) {
        Page<RemarksResult> pageContext = getPageContext();
        IPage<RemarksResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 个人动态
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo personalDynamic(RemarksParam param) {
        param.setCreateUser(LoginContextHolder.getContext().getUserId());
        param.setType("dynamic");
        Page<RemarksResult> pageContext = getPageContext();
        IPage<RemarksResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    private Serializable getKey(RemarksParam param) {
        return param.getRemarksId();
    }

    private Page<RemarksResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Remarks getOldEntity(RemarksParam param) {
        return this.getById(getKey(param));
    }

    private Remarks getEntity(RemarksParam param) {
        Remarks entity = new Remarks();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    void format(List<RemarksResult> data) {
        List<Long> taskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<String> mediaIds = new ArrayList<>();

        for (RemarksResult datum : data) {
            userIds.add(datum.getCreateUser());
            taskIds.add(datum.getTaskId());
            mediaIds.add(datum.getPhotoId());
        }
        List<ActivitiProcessTask> tasks = taskIds.size() == 0 ? new ArrayList<>() : taskService.listByIds(taskIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);


        for (RemarksResult datum : data) {
            for (User user : userList) {
                if (ToolUtil.isNotEmpty(datum.getCreateUser()) && user.getUserId().equals(datum.getCreateUser())) {
                    String imgUrl = stepsService.imgUrl(user.getUserId().toString());
                    user.setAvatar(imgUrl);
                    datum.setUser(user);
                    break;
                }
            }
            for (ActivitiProcessTask task : tasks) {
                if (ToolUtil.isNotEmpty(datum.getTaskId()) && datum.getTaskId().equals(task.getProcessTaskId())) {
                    ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
                    ToolUtil.copyProperties(task, taskResult);
                    datum.setTaskResult(taskResult);
                    break;
                }
            }
        }
    }


    @Override
    public void addNote(AuditParam auditParam) {

        List<ActivitiProcessLog> logs = logService.listByTaskId(auditParam.getTaskId());
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : logs) {
            stepIds.add(activitiProcessLog.getSetpsId());
        }
        List<ActivitiAudit> activitiAudits = stepIds.size() == 0 ? new ArrayList<>() : this.auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepIds);
        }});
        //TODO 更换新的审批流查询逻辑
        List<ActivitiProcessLog> audit = logService.getAudit(auditParam.getTaskId());
        for (ActivitiProcessLog activitiProcessLog : audit) {
            ActivitiAudit activitiAudit = logService.getRule(activitiAudits, activitiProcessLog.getSetpsId());
            AuditRule rule = activitiAudit.getRule();
            if (activitiAudit.getType().equals("process") && rule.getType().toString().equals("audit")) {
                if (logService.checkUser(activitiAudit.getRule(), null)) {
                    Remarks remarks = new Remarks();
                    remarks.setContent(auditParam.getNote());
                    remarks.setUserIds(auditParam.getUserIds());
                    remarks.setTaskId(auditParam.getTaskId());
                    remarks.setType(RemarkEnum.audit.name());
                    remarks.setStatus(auditParam.getStatus());
                    remarks.setPhotoId(auditParam.getPhotoId());
                    remarks.setLogId(activitiProcessLog.getLogId());
                    this.save(remarks);
                }
            }
        }
    }

    @Override
    public void addComments(AuditParam auditParam) {
        Remarks remarks = new Remarks();
        remarks.setTaskId(auditParam.getTaskId());
        remarks.setContent(auditParam.getNote());
        remarks.setType(RemarkEnum.comments.name());
        remarks.setPhotoId(auditParam.getPhotoId());
        remarks.setPid(auditParam.getPid());
        remarks.setUserIds(auditParam.getUserIds());
        this.save(remarks);

        /**
         * 添加动态
         */
        ActivitiProcessTask processTask = taskService.getById(auditParam.getTaskId());
        if (ToolUtil.isNotEmpty(processTask)) {
            shopCartService.addDynamic(processTask.getFormId(), null, "发布了评论");
        }

        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setFunction(MarkDownTemplateTypeEnum.toPerson);
            setType(2);
            setItems("收到评论");
            setDescription(LoginContextHolder.getContext().getUser().getName() + "发布了评论");
            setCreateTime(remarks.getCreateTime());
            setTaskId(processTask.getProcessTaskId());
            setDescription(remarks.getContent());
            setSource("processTask");
            setSourceId(processTask.getProcessTaskId());
            setUserId(remarks.getCreateUser());
            setUrl(mobileService.getMobileConfig().getUrl() + "/#/Receipts/ReceiptsDetail?id=" + processTask.getProcessTaskId());
            setUserIds(new ArrayList<Long>() {{
                add(processTask.getCreateUser());
            }});
            setCreateUser(remarks.getCreateUser());

        }});


        if (ToolUtil.isNotEmpty(auditParam.getUserIds())) {
            String[] split = auditParam.getUserIds().split(",");
            List<Long> userIds = new ArrayList<>();
            for (String s : split) {
                userIds.add(Long.valueOf(s));
            }
            userIds.add(processTask.getCreateUser());
            userIds = userIds.stream().distinct().collect(Collectors.toList());
            pushPeople(userIds, auditParam.getTaskId(), remarks);
        }

    }

    @Override
    public void addByMQ(RemarksParam remarksParam) {
        Remarks entity = this.getEntity(remarksParam);
        this.save(entity);
        try {
            if (ToolUtil.isNotEmpty(remarksParam.getUserIds())) {
                String[] split = remarksParam.getUserIds().split(",");
                List<Long> userIds = new ArrayList<>();
                for (String s : split) {
                    userIds.add(Long.valueOf(s));
                }
                pushPeople(userIds, remarksParam.getTaskId(), entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定人推送
     *
     * @param userIds
     * @param taskId
     */
    @Override
    public void pushPeople(List<Long> userIds, Long taskId, Remarks remarks) {
        ActivitiProcessTask task = taskService.getById(taskId);
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setFunction(MarkDownTemplateTypeEnum.atPerson);
            setType(2);
            setItems("收到评论");
            setDescription("有人在 " + task.getTaskName() + " 中@了你");
            setCreateTime(remarks.getCreateTime());
            setTaskId(taskId);
            setDescription(remarks.getContent());
            setSource("processTask");
            setSourceId(taskId);
            setUserId(remarks.getCreateUser());
            setUrl(mobileService.getMobileConfig().getUrl() + "/#/Receipts/ReceiptsDetail?id=" + taskId);
            setUserIds(userIds);
            setCreateUser(task.getCreateUser());

        }});

    }


    @Override
    public List<RemarksResult> getComments(Long taskId) {
        List<Remarks> remarks = this.query().eq("task_id", taskId).orderByDesc("create_time").isNull("pid").eq("display", 1).list();
        List<RemarksResult> remarksResults = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        for (Remarks remark : remarks) {
            ids.add(remark.getRemarksId());
            RemarksResult result = new RemarksResult();
            ToolUtil.copyProperties(remark, result);
            userIds.add(remark.getCreateUser());
            remarksResults.add(result);
        }

        List<Remarks> childs = ids.size() == 0 ? new ArrayList<>() : this.query().in("pid", ids).eq("display", 1).list();
        List<RemarksResult> results = BeanUtil.copyToList(childs, RemarksResult.class);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);


        for (User user : userList) {    //动态人头像
            String imgUrl = appStepService.imgUrl(user.getUserId().toString());
            user.setAvatar(imgUrl);
        }
        /**
         * 回复
         */
        formatUrl(results);
        formatUrl(remarksResults);

        for (RemarksResult remarksResult : remarksResults) {

            for (User user : userList) {
                if (remarksResult.getCreateUser().equals(user.getUserId())) {
                    remarksResult.setUser(user);
                }
            }

            List<RemarksResult> children = new ArrayList<>();
            for (RemarksResult result : results) {
                if (remarksResult.getRemarksId().equals(result.getPid())) {
                    children.add(result);
                }
            }
            remarksResult.setChildrens(children);

        }
        return remarksResults;
    }

    private void formatUrl(List<RemarksResult> data) {

        List<User> userList = userService.list();
        for (RemarksResult datum : data) {
            for (User user : userList) {
                if (datum.getCreateUser().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }
            if (ToolUtil.isNotEmpty(datum.getPhotoId())) {
                StringBuffer stringBuffer = new StringBuffer();
                List<String> photoUrl = ToolUtil.isEmpty(datum.getPhotoId()) ? new ArrayList<>() : Arrays.asList(datum.getPhotoId().split(","));
                for (String id : photoUrl) {
                    String url = mediaService.getMediaUrl(Long.valueOf(id), 1L);
                    stringBuffer.append(url + ",");
                }
                String substring = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                datum.setPhotoId(substring);
            }
        }
    }

    @Override
    public void addDynamic(String content) {
        RemarksParam remarksParam = new RemarksParam();
        remarksParam.setTaskId(0L);
        remarksParam.setType("dynamic");
        remarksParam.setCreateUser(LoginContextHolder.getContext().getUserId());
        remarksParam.setContent(content);
        Remarks entity = this.getEntity(remarksParam);
        ToolUtil.copyProperties(remarksParam, entity);
        this.save(entity);
    }
}

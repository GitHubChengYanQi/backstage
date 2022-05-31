package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.mapper.RemarksMapper;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import cn.atsoft.dasheng.form.pojo.AuditParam;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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

    @Override
    public void add(Long logId, String note) {
        Remarks remarks = new Remarks();
        remarks.setLogId(logId);
        remarks.setContent(note);
        this.save(remarks);
    }

    @Override
    public void delete(RemarksParam param) {
        this.removeById(getKey(param));
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
    public PageInfo<RemarksResult> findPageBySpec(RemarksParam param) {
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
        for (RemarksResult datum : data) {
            taskIds.add(datum.getTaskId());
        }
        List<ActivitiProcessTask> tasks = taskIds.size() == 0 ? new ArrayList<>() : taskService.listByIds(taskIds);

        for (RemarksResult datum : data) {
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
        List<ActivitiProcessLog> audit = logService.getAudit(auditParam.getTaskId());
        for (ActivitiProcessLog activitiProcessLog : audit) {
            ActivitiAudit activitiAudit = logService.getRule(activitiAudits, activitiProcessLog.getSetpsId());
            AuditRule rule = activitiAudit.getRule();
            if (activitiAudit.getType().equals("process") && rule.getType().toString().equals("audit")) {

                if (logService.checkUser(activitiAudit.getRule())) {
                    Remarks remarks = new Remarks();
                    remarks.setContent(auditParam.getNote());
                    remarks.setUserIds(auditParam.getUserIds());
                    remarks.setTaskId(auditParam.getTaskId());
                    remarks.setType("备注");
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
        LoginUser user = LoginContextHolder.getContext().getUser();

        Remarks remarks = new Remarks();
        remarks.setTaskId(auditParam.getTaskId());
        remarks.setContent(auditParam.getNote());
        remarks.setType("评论");
        remarks.setPhotoId(auditParam.getPhotoId());
        remarks.setUserIds(user.getId().toString());
        this.save(remarks);

        if (ToolUtil.isNotEmpty(auditParam.getUserIds())) {
            String[] split = auditParam.getUserIds().split(",");
            List<Long> userIds = new ArrayList<>();
            for (String s : split) {
                userIds.add(Long.valueOf(s));
            }
            pushPeople(userIds, auditParam.getTaskId());
        }
    }

    /**
     * 指定人推送
     *
     * @param userIds
     * @param taskId
     */
    @Override
    public void pushPeople(List<Long> userIds, Long taskId) {
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setDescription("你有一条被@的消息");
        wxCpTemplate.setTitle("新消息");
        wxCpTemplate.setUserIds(userIds);
        wxCpTemplate.setUrl(mobileService.getMobileConfig().getUrl() + "/#/Work/Workflow?id=" + taskId);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }


    @Override
    public List<RemarksResult> getComments(Long taskId) {

        List<Remarks> remarks = this.query().eq("task_id", taskId).eq("type", "评论").orderByDesc("create_time").list();

        List<RemarksResult> remarksResults = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        for (Remarks remark : remarks) {
            RemarksResult result = new RemarksResult();
            ToolUtil.copyProperties(remark, result);
            remarksResults.add(result);
            userIds.add(Long.valueOf(remark.getUserIds()));
        }
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);


        for (RemarksResult remarksResult : remarksResults) {
            for (User user : userList) {
                if (remarksResult.getUserIds().equals(user.getUserId().toString())) {
                    remarksResult.setUser(user);
                }
            }
        }
        for (RemarksResult remarksResult : remarksResults) {
            if (ToolUtil.isNotEmpty(remarksResult.getPhotoId())) {
                StringBuffer stringBuffer = new StringBuffer();
                List<String> photoUrl = ToolUtil.isEmpty(remarksResult.getPhotoId()) ? new ArrayList<>() : Arrays.asList(remarksResult.getPhotoId().split(","));
                for (String id : photoUrl) {
                    String url = mediaService.getMediaUrl(Long.valueOf(id), 1L);
                    stringBuffer.append(url + ",");
                }
                String substring = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                remarksResult.setPhotoId(substring);
            }

        }

        return remarksResults;
    }
}

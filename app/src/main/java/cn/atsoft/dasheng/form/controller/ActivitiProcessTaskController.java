package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.erp.service.impl.QualityTaskServiceImpl;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.results.SetpsDetailResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 流程任务表控制器
 *
 * @author Jazz
 * @Date 2021-11-19 08:56:11
 */
@RestController
@RequestMapping("/activitiProcessTask")
@Api(tags = "流程任务表")
public class ActivitiProcessTaskController extends BaseController {

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiStepsService activitiStepsService;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private ActivitiProcessService activitiProcessService;

    /**
     * 新增接口
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiProcessTaskParam activitiProcessTaskParam) {
        this.activitiProcessTaskService.add(activitiProcessTaskParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ActivitiProcessTaskParam activitiProcessTaskParam) {

        this.activitiProcessTaskService.update(activitiProcessTaskParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ActivitiProcessTaskParam activitiProcessTaskParam) {
        this.activitiProcessTaskService.delete(activitiProcessTaskParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ActivitiProcessTaskResult> detail(@RequestBody ActivitiProcessTaskParam activitiProcessTaskParam) {
        ActivitiProcessTask detail = this.activitiProcessTaskService.getById(activitiProcessTaskParam.getProcessTaskId());
        ActivitiProcessTaskResult result = new ActivitiProcessTaskResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ActivitiProcessTaskResult> list(@RequestBody(required = false) ActivitiProcessTaskParam activitiProcessTaskParam) {


        if (ToolUtil.isEmpty(activitiProcessTaskParam)) {
            activitiProcessTaskParam = new ActivitiProcessTaskParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.activitiProcessTaskService.findPageBySpec(activitiProcessTaskParam);
        } else {
            Long deptId = LoginContextHolder.getContext().getUser().getDeptId();
            Long userId = LoginContextHolder.getContext().getUserId();
            activitiProcessTaskParam.setUserId(userId);
            activitiProcessTaskParam.setDeptId(deptId);
            return this.activitiProcessTaskService.findPageBySpec(activitiProcessTaskParam);
        }
    }

    @RequestMapping(value = "/auditDetail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SetpsDetailResult> auditDetail(@RequestBody ActivitiProcessTaskParam activitiProcessTaskParam) {
        LoginUser loginUser = LoginContextHolder.getContext().getUser();
        ActivitiAudit audit = auditService.query().eq("setps_id", activitiProcessTaskParam.getSetpsId()).one();

        Boolean userFlag = false;
        Boolean deptFlag = false;
        if (ToolUtil.isNotEmpty(audit.getRule()) && ToolUtil.isNotEmpty(audit.getRule().getStartUsers()) && ToolUtil.isNotEmpty(audit.getRule().getStartUsers().getUsers())) {
            for (StartUsers.Users user : audit.getRule().getStartUsers().getUsers()) {
                if (user.getKey().equals(loginUser.getId().toString())) {
                    userFlag = true;
                }
            }

        }
        if (ToolUtil.isNotEmpty(audit.getRule()) && ToolUtil.isNotEmpty(audit.getRule().getStartUsers()) && ToolUtil.isNotEmpty(audit.getRule().getStartUsers().getDepts())) {
            for (StartUsers.Depts dept : audit.getRule().getStartUsers().getDepts()) {
                if (dept.getKey().equals(loginUser.getDeptId().toString())) {
                    deptFlag = true;
                }
            }
        }


        QualityTask detail = this.qualityTaskService.getById(activitiProcessTaskParam.getQTaskId());
        QualityTaskResult result = new QualityTaskResult();
        ToolUtil.copyProperties(detail, result);
        qualityTaskService.detailFormat(result);
        ActivitiAuditResult activitiAuditResult = new ActivitiAuditResult();
        ToolUtil.copyProperties(audit, activitiAuditResult);
        SetpsDetailResult setpsDetailResult = new SetpsDetailResult();
        setpsDetailResult.setAuditResult(activitiAuditResult);

        if (ToolUtil.isNotEmpty(result.getCreateUser())) {
            User user = userService.getById(result.getCreateUser());
            result.setCreateName(user.getName());
        }

        setpsDetailResult.setQualityTaskResult(result);
        ActivitiSteps setpsId = activitiStepsService.query().eq("setps_id", audit.getSetpsId()).one();
        List<ActivitiSteps> activitiSteps = activitiStepsService.lambdaQuery().in(ActivitiSteps::getProcessId, setpsId.getProcessId()).list();
        List<Long> activitiStepsIds = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            activitiStepsIds.add(activitiStep.getSetpsId());
        }
        ActivitiProcessTask activitiProcess = activitiProcessTaskService.query().eq("form_id", activitiProcessTaskParam.getQTaskId()).one();
        List<ActivitiAudit> activitiAudits = auditService.lambdaQuery().in(ActivitiAudit::getSetpsId, activitiStepsIds).list();
        List<ActivitiProcessLog> activitiProcessLogs = activitiProcessLogService.lambdaQuery().in(ActivitiProcessLog::getSetpsId, activitiStepsIds).and(i -> i.eq(ActivitiProcessLog::getTaskId, activitiProcess.getProcessTaskId())).list();
        List<ActivitiAuditResult> logResult = new ArrayList<>();
        for (ActivitiAudit activitiAudit : activitiAudits) {
            ActivitiAuditResult auditResult = new ActivitiAuditResult();
            ToolUtil.copyProperties(activitiAudit, auditResult);
            for (ActivitiProcessLog activitiProcessLog : activitiProcessLogs) {
                if (activitiAudit.getSetpsId().equals(activitiProcessLog.getSetpsId())) {
                    auditResult.setStatus(activitiProcessLog.getStatus());
                }
            }
            logResult.add(auditResult);
        }
        setpsDetailResult.setAuditResults(logResult);

        if (ToolUtil.isNotEmpty(setpsId.getProcessId())) {
            ActivitiProcess process = activitiProcessService.getById(setpsId.getProcessId());
            setpsDetailResult.setActivitiProcess(process);
        }
        if (!userFlag && !deptFlag) {
            if (!detail.getUserId().equals(loginUser.getId())) {
                throw new ServiceException(500, "抱歉该审批任务与您无关，您无权限查看");
            }
        }
        return ResponseData.success(setpsDetailResult);
    }

}



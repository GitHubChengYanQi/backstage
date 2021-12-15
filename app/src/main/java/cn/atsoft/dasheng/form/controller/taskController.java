package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/audit")
@Api(tags = "流程主表")
public class taskController extends BaseController {

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ActivitiProcessTaskService taskService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivitiProcessLogService logService;

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData audit(@Param("taskId") Long taskId, @Param("status") Integer status) {
        this.activitiProcessLogService.audit(taskId, status);
        return ResponseData.success();
    }

    private ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId) {
        for (ActivitiAudit activitiAudit : activitiAudits) {
            if (activitiAudit.getSetpsId().equals(stepId)) {
                return activitiAudit;
            }
        }
        return null;
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData<ActivitiProcessTaskResult> detail(@Param("taskId") Long taskId) {
        //流程任务
        ActivitiProcessTask processTask = taskService.getById(taskId);
        ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
        ToolUtil.copyProperties(processTask, taskResult);
        //树形结构
        ActivitiStepsResult stepResult = stepsService.getStepResult(taskResult.getProcessId());
        //获取当前processTask 下的所有log
        List<ActivitiProcessLogResult> process = logService.getLogByTaskProcess(processTask.getProcessId(), taskId);
        //比对log
        ActivitiStepsResult stepLog = stepsService.getStepLog(stepResult, process);

        //取出所有未审核节点
        List<ActivitiProcessLog> audit = activitiProcessLogService.getAudit(taskId);

        /**
         * 流程中审核节点
         */
        List<Long> stepsIds = new ArrayList<>();
        for (ActivitiProcessLogResult processLog : process) {
            stepsIds.add(processLog.getSetpsId());
        }
        /**
         * 取出所有步骤配置
         */
        List<ActivitiAudit> activitiAudits = auditService.list(new QueryWrapper<ActivitiAudit>() {{
            in("setps_id", stepsIds);
        }});

        taskResult.setPermissions(false);
        for (ActivitiProcessLog activitiProcessLog : audit) {
            if (activitiProcessLog.getStatus() == -1) {
                /**
                 * 取节点规则
                 */
                ActivitiAudit activitiAudit = getRule(activitiAudits, activitiProcessLog.getSetpsId());

                if (ToolUtil.isNotEmpty(activitiAudit) && ToolUtil.isNotEmpty(activitiAudit.getRule()) && activitiAudit.getRule().getType() == RuleType.audit && activitiProcessLogService.checkUser(activitiAudit.getRule())) {
                    taskResult.setPermissions(true);
                    break;
                }
            }

        }

        taskResult.setStepsResult(stepLog);
        if (ToolUtil.isNotEmpty(taskResult.getCreateUser())){
            User user = userService.getById(taskResult.getCreateUser());
            taskResult.setCreateName(user.getName());
        }
        return ResponseData.success(taskResult);

//        //质检任务
//        QualityTask qualityTask = this.qualityTaskService.getById(taskResult.getFormId());
//        if (ToolUtil.isEmpty(qualityTask) || ToolUtil.isEmpty(processTask)){
//            return null;
//        }
//        QualityTaskResult qualityTaskResult = new QualityTaskResult();
//        ToolUtil.copyProperties(qualityTask, qualityTaskResult);
//        User user = userService.getOne(new QueryWrapper<User>() {{
//            eq("user_id", qualityTaskResult.getCreateUser());
//        }});
//        qualityTaskResult.setCreateName(user.getName());
//        qualityTaskResult.setActivitiProcessTaskResult(taskResult);
//
//
//        ActivitiProcess process = processService.getOne(new QueryWrapper<ActivitiProcess>() {{
//            eq("process_id", processTask.getProcessId());
//        }});
//        qualityTaskResult.setProcess(process);
//
//        List<ActivitiProcessLog> processLogList = logService.list(new QueryWrapper<ActivitiProcessLog>() {{
//            eq("task_id", taskId);
//        }});
//
//
//        List<Long> stepIds = new ArrayList<>();
//        for (ActivitiProcessLog activitiProcessLog : processLogList) {
//            stepIds.add(activitiProcessLog.getSetpsId());
//        }
//
//        List<ActivitiStepsResult> resultList = stepsService.backSteps(stepIds);
//
//        List<ActivitiProcessLogResult> processLogResults = new ArrayList<>();
//
//        for (ActivitiProcessLog activitiProcessLog : processLogList) {
//
//            for (ActivitiStepsResult activitiStepsResult : resultList) {
//
//                if (activitiProcessLog.getSetpsId().equals(activitiStepsResult.getSetpsId())) {
//
//                    ActivitiProcessLogResult activitiProcessLogResult = new ActivitiProcessLogResult();
//                    ToolUtil.copyProperties(activitiProcessLog, activitiProcessLogResult);
//                    activitiProcessLogResult.setStepsResult(activitiStepsResult);
//                    processLogResults.add(activitiProcessLogResult);
//                }
//            }
//
//        }
//        qualityTaskResult.setLogResults(processLogResults);
//
//        return ResponseData.success(qualityTaskResult);
    }
}

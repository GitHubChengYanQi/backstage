package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.response.ResponseData;
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
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private QualityTaskService qualityTaskService;

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData audit(@Param("taskId") Long taskId, @Param("status") Integer status) {
        this.activitiProcessLogService.add(taskId, status);
        return ResponseData.success();
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData<QualityTaskResult> detail(@Param("taskId") Long taskId) {

        //流程任务
        ActivitiProcessTask processTask = taskService.getById(taskId);
        ActivitiProcessTaskResult taskResult = new ActivitiProcessTaskResult();
        ToolUtil.copyProperties(processTask, taskResult);

        //质检任务
        QualityTask qualityTask = this.qualityTaskService.getById(taskResult.getFormId());
        QualityTaskResult qualityTaskResult = new QualityTaskResult();
        ToolUtil.copyProperties(qualityTask, qualityTaskResult);

        qualityTaskResult.setActivitiProcessTaskResult(taskResult);

        List<ActivitiProcessLog> logs = this.activitiProcessLogService.getAudit(taskId);
        List<Long> stepIds = new ArrayList<>();
        for (ActivitiProcessLog activitiProcessLog : logs) {
            stepIds.add(activitiProcessLog.getSetpsId());
        }

        List<ActivitiSteps> steps = stepsService.list(new QueryWrapper<ActivitiSteps>() {{
            eq("process_id", processTask.getProcessId());
        }});

        List<ActivitiStepsResult> stepsResults = new ArrayList<>();
        for (ActivitiSteps step : steps) {
            ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
            ToolUtil.copyProperties(step, activitiStepsResult);
            ActivitiAuditResult serviceAudit = auditService.getAudit(activitiStepsResult.getSetpsId());
            activitiStepsResult.setServiceAudit(serviceAudit);
            stepsResults.add(activitiStepsResult);
        }
        qualityTaskResult.setSteps(stepsResults);

        ActivitiProcess process = processService.getOne(new QueryWrapper<ActivitiProcess>() {{
            eq("process_id", processTask.getProcessId());
        }});
        qualityTaskResult.setProcess(process);

        if (ToolUtil.isNotEmpty(stepIds)) {
            List<ActivitiAudit> audits = auditService.list(new QueryWrapper<ActivitiAudit>() {{
                in("setps_id", stepIds);
            }});


            qualityTaskResult.setAudits(audits);
        }

        return ResponseData.success(qualityTaskResult);
    }
}

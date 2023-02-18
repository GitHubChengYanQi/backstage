package cn.atsoft.dasheng.audit.controller;

import cn.atsoft.dasheng.audit.model.params.AuditParam;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newAudit/{version}")
@ApiVersion("3.0")
@Api(tags = "流程主表")
public class AuditController {

    @Autowired
    private ActivitiProcessFormLogService activitiProcessFormLogService;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData audit(@RequestBody AuditParam auditParam) {
        //添加备注
//        remarksService.addNote(auditParam);
//        this.activitiProcessLogService.judgeLog(auditParam.getTaskId(), auditParam.getLogIds());  //判断当前log状态
        this.activitiProcessFormLogService.audit(auditParam.getTaskId(), auditParam.getStatus(), LoginContextHolder.getContext().getUserId());

        return ResponseData.success();
    }
}

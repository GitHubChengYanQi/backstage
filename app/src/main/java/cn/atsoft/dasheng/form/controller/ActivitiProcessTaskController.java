package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


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
    private ActivitiProcessTaskSend taskSend;
    @Autowired
    private ActivitiAuditService activitiAuditService;

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

    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    @ApiOperation("")
    public ResponseData getUsers(@RequestBody ActivitiAuditParam activitiAuditParam) {
        ActivitiAudit audit = this.activitiAuditService.getById(activitiAuditParam.getAuditId());
        List<Long> longs = taskSend.selectUsers(audit.getRule());
        return ResponseData.success(longs);
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


    @RequestMapping(value = "/getTaskIdByFromId", method = RequestMethod.GET)
    public ResponseData getTaskIdByFromId( Long formId, String type) {
        if (ToolUtil.isEmpty(formId)||ToolUtil.isEmpty(type)) {
            throw new ServiceException(500,"缺少参数");
        }
        ActivitiProcessTask task = this.activitiProcessTaskService.query().eq("form_id", formId)
                .eq("type", type)
                .eq("display", 1).one();
        if (ToolUtil.isEmpty(task)) {
            task = new ActivitiProcessTask();
        }
        return ResponseData.success(task.getProcessTaskId());
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

}



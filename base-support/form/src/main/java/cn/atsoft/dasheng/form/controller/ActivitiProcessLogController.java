package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 流程日志表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiProcessLog")
@Api(tags = "流程日志表")
public class ActivitiProcessLogController extends BaseController {

    @Autowired
    private ActivitiProcessFormLogService activitiProcessFormLogService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {
        this.activitiProcessFormLogService.add(activitiProcessLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {

        this.activitiProcessFormLogService.update(activitiProcessLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {
        this.activitiProcessFormLogService.delete(activitiProcessLogParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {
        ActivitiProcessLog detail = this.activitiProcessFormLogService.getById(activitiProcessLogParam.getLogId());
        ActivitiProcessLogResult result = new ActivitiProcessLogResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ActivitiProcessLogParam activitiProcessLogParam) {
        if (ToolUtil.isEmpty(activitiProcessLogParam)) {
            activitiProcessLogParam = new ActivitiProcessLogParam();
        }
        return this.activitiProcessFormLogService.findPageBySpec(activitiProcessLogParam);
    }





}



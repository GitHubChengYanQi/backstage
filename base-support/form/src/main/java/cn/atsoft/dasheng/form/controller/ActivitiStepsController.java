package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 流程步骤表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiSteps")
@Api(tags = "流程步骤表")
public class ActivitiStepsController extends BaseController {

    @Autowired
    private ActivitiStepsService activitiStepsService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiStepsParam activitiStepsParam) {
        this.activitiStepsService.addProcess(activitiStepsParam);
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
    public ResponseData update(@RequestBody ActivitiStepsParam activitiStepsParam) {

        this.activitiStepsService.update(activitiStepsParam);
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
    public ResponseData delete(@RequestBody ActivitiStepsParam activitiStepsParam) {
        this.activitiStepsService.delete(activitiStepsParam);
        return ResponseData.success();
    }

    /**
     * 通过类型查询流程
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/getStepResultByType", method = RequestMethod.GET)
    public ResponseData getStepResultByType(@Param("type") String type) {
        return ResponseData.success(this.activitiStepsService.getStepResultByType(type));
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ActivitiStepsParam activitiStepsParam) {
        ActivitiStepsResult steps = activitiStepsService.getStepResult(activitiStepsParam.getProcessId());
//        ActivitiStepsResult stepsResult = this.activitiStepsService.backStepsResult(activitiStepsParam.getProcessId());
        return ResponseData.success(steps);
    }

    /**
     * 查询列表,
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ActivitiStepsParam activitiStepsParam) {
        if (ToolUtil.isEmpty(activitiStepsParam)) {
            activitiStepsParam = new ActivitiStepsParam();
        }
        return this.activitiStepsService.findPageBySpec(activitiStepsParam);
    }


}



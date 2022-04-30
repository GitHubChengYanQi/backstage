package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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


}



package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.pojo.ProcessParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


/**
 * 流程主表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiProcess")
@Api(tags = "流程主表")
public class ActivitiProcessController extends BaseController {

    @Autowired
    private ActivitiProcessService activitiProcessService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiProcessParam activitiProcessParam) {
        if (ToolUtil.isEmpty(activitiProcessParam.getType()) || ToolUtil.isEmpty(activitiProcessParam.getModule())) {
            throw new ServiceException(500, "请确定单据或类型");
        }
        this.activitiProcessService.add(activitiProcessParam);
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
    public ResponseData update(@RequestBody ActivitiProcessParam activitiProcessParam) {

        this.activitiProcessService.update(activitiProcessParam);
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
    public ResponseData delete(@RequestBody ActivitiProcessParam activitiProcessParam) {
        this.activitiProcessService.delete(activitiProcessParam);
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
    public ResponseData detail(@RequestBody ActivitiProcessParam activitiProcessParam) {
        ActivitiProcess detail = this.activitiProcessService.getById(activitiProcessParam.getProcessId());
        ActivitiProcessResult result = new ActivitiProcessResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<ActivitiProcessResult> list(@RequestBody(required = false) ActivitiProcessParam activitiProcessParam) {
        if (ToolUtil.isEmpty(activitiProcessParam)) {
            activitiProcessParam = new ActivitiProcessParam();
        }
        return this.activitiProcessService.findPageBySpec(activitiProcessParam);
    }


    @RequestMapping(value = "/getType", method = RequestMethod.GET)
    public ResponseData getType() {
        List<String> type = this.activitiProcessService.getType();
        return ResponseData.success(type);
    }


    @RequestMapping(value = "/getModule", method = RequestMethod.POST)
    public ResponseData getModule(@RequestBody ProcessParam param) {
        List<String> module = this.activitiProcessService.getModule(param.getProcessEnum());
        return ResponseData.success(module);
    }


}



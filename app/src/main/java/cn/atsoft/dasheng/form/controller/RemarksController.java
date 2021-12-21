package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * log备注控制器
 *
 * @author song
 * @Date 2021-12-16 13:38:54
 */
@RestController
@RequestMapping("/remarks")
@Api(tags = "log备注")
public class RemarksController extends BaseController {

    @Autowired
    private RemarksService remarksService;

    @Autowired
    private ActivitiProcessLogService logService;


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<RemarksResult> detail(@RequestBody RemarksParam remarksParam) {
        Remarks detail = this.remarksService.getById(remarksParam.getRemarksId());
        RemarksResult result = new RemarksResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RemarksResult> list(@RequestBody(required = false) RemarksParam remarksParam) {
        if (ToolUtil.isEmpty(remarksParam)) {
            remarksParam = new RemarksParam();
        }
        return this.remarksService.findPageBySpec(remarksParam);
    }


    /**
     * 查询审核
     *
     * @author
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/auditList", method = RequestMethod.POST)
    @ApiOperation("我所有未审核的")
    public ResponseData auditList(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {

        List<ActivitiProcessLogResult> logResults = logService.auditList(activitiProcessLogParam);
        return ResponseData.success(logResults);
    }

    /**
     * 查询推送
     *
     * @author
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/sendList", method = RequestMethod.POST)
    @ApiOperation("我所有推送的")
    public ResponseData sendList(@RequestBody ActivitiProcessLogParam activitiProcessLogParam) {
        List<ActivitiProcessLogResult> logResults = logService.sendList(activitiProcessLogParam);
        return ResponseData.success(logResults);
    }
}



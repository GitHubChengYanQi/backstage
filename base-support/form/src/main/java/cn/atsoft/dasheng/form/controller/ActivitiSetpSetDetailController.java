package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
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
 * 工序步骤详情表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiSetpSetDetail")
@Api(tags = "工序步骤详情表")
public class ActivitiSetpSetDetailController extends BaseController {

    @Autowired
    private ActivitiSetpSetDetailService activitiSetpSetDetailService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiSetpSetDetailParam activitiSetpSetDetailParam) {
        this.activitiSetpSetDetailService.add(activitiSetpSetDetailParam);
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
    public ResponseData update(@RequestBody ActivitiSetpSetDetailParam activitiSetpSetDetailParam) {

        this.activitiSetpSetDetailService.update(activitiSetpSetDetailParam);
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
    public ResponseData delete(@RequestBody ActivitiSetpSetDetailParam activitiSetpSetDetailParam)  {
        this.activitiSetpSetDetailService.delete(activitiSetpSetDetailParam);
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
    public ResponseData detail(@RequestBody ActivitiSetpSetDetailParam activitiSetpSetDetailParam) {
        ActivitiSetpSetDetail detail = this.activitiSetpSetDetailService.getById(activitiSetpSetDetailParam.getDetailId());
        ActivitiSetpSetDetailResult result = new ActivitiSetpSetDetailResult();
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
    public PageInfo list(@RequestBody(required = false) ActivitiSetpSetDetailParam activitiSetpSetDetailParam) {
        if(ToolUtil.isEmpty(activitiSetpSetDetailParam)){
            activitiSetpSetDetailParam = new ActivitiSetpSetDetailParam();
        }
        return this.activitiSetpSetDetailService.findPageBySpec(activitiSetpSetDetailParam);
    }




}



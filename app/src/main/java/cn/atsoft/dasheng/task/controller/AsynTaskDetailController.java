package cn.atsoft.dasheng.task.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.task.entity.AsynTaskDetail;
import cn.atsoft.dasheng.task.model.params.AsynTaskDetailParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskDetailResult;
import cn.atsoft.dasheng.task.service.AsynTaskDetailService;
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
 * 任务详情表控制器
 *
 * @author song
 * @Date 2022-04-26 13:47:51
 */
@RestController
@RequestMapping("/asynTaskDetail")
@Api(tags = "任务详情表")
public class AsynTaskDetailController extends BaseController {

    @Autowired
    private AsynTaskDetailService asynTaskDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AsynTaskDetailParam asynTaskDetailParam) {
        this.asynTaskDetailService.add(asynTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AsynTaskDetailParam asynTaskDetailParam) {

        this.asynTaskDetailService.update(asynTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AsynTaskDetailParam asynTaskDetailParam) {
        this.asynTaskDetailService.delete(asynTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData remove(@RequestParam("id") Long id) {
        AsynTaskDetail taskDetail = this.asynTaskDetailService.getById(id);
        if (ToolUtil.isEmpty(taskDetail)) {
            throw new ServiceException(500, "参数不正确");
        }
        taskDetail.setDisplay(0);
        asynTaskDetailService.updateById(taskDetail);
        return ResponseData.success();
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AsynTaskDetailResult> detail(@RequestBody AsynTaskDetailParam asynTaskDetailParam) {
        AsynTaskDetail detail = this.asynTaskDetailService.getById(asynTaskDetailParam.getDetailId());
        AsynTaskDetailResult result = new AsynTaskDetailResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AsynTaskDetailResult> list(@RequestBody(required = false) AsynTaskDetailParam asynTaskDetailParam) {
        if (ToolUtil.isEmpty(asynTaskDetailParam)) {
            asynTaskDetailParam = new AsynTaskDetailParam();
        }
        return this.asynTaskDetailService.findPageBySpec(asynTaskDetailParam);
    }


}



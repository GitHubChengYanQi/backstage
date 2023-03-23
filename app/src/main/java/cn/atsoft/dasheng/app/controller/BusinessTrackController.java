package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.TrackNumberResult;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * 跟进内容控制器
 *
 * @author cheng
 * @Date 2021-09-17 10:35:56
 */
@RestController
@RequestMapping("/businessTrack")
@Api(tags = "跟进内容")
public class BusinessTrackController extends BaseController {

    @Autowired
    private BusinessTrackService businessTrackService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BusinessTrackParam businessTrackParam) {
        this.businessTrackService.add(businessTrackParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BusinessTrackParam businessTrackParam) {

        this.businessTrackService.update(businessTrackParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BusinessTrackParam businessTrackParam) {
        this.businessTrackService.delete(businessTrackParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody BusinessTrackParam businessTrackParam) {
        BusinessTrack detail = this.businessTrackService.getById(businessTrackParam.getTrackId());
        BusinessTrackResult result = new BusinessTrackResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) BusinessTrackParam businessTrackParam) {
        if (ToolUtil.isEmpty(businessTrackParam)) {
            businessTrackParam = new BusinessTrackParam();
        }
//        return this.businessTrackService.findPageBySpec(businessTrackParam);
        return this.businessTrackService.findPageBySpec(businessTrackParam, null);

    }

    /**
     * 查询分类数量
     *
     * @author cheng
     * @Date 2021-09-17
     */
    @Permission
    @RequestMapping(value = "/listNumber", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData listNumber() {
        TrackNumberResult trackServiceNumber = businessTrackService.findNumber();
        return ResponseData.success(trackServiceNumber);
    }
}



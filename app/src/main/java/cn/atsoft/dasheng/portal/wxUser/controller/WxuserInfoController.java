package cn.atsoft.dasheng.portal.wxUser.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
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
 * 用户小程序关联控制器
 *
 * @author 
 * @Date 2021-08-24 14:40:25
 */
@RestController
@RequestMapping("/wxuserInfo")
@Api(tags = "用户小程序关联")
public class WxuserInfoController extends BaseController {

    @Autowired
    private WxuserInfoService wxuserInfoService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxuserInfoParam wxuserInfoParam) {
        this.wxuserInfoService.add(wxuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxuserInfoParam wxuserInfoParam) {

        this.wxuserInfoService.update(wxuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxuserInfoParam wxuserInfoParam)  {
        this.wxuserInfoService.delete(wxuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxuserInfoResult> detail(@RequestBody WxuserInfoParam wxuserInfoParam) {
        WxuserInfo detail = this.wxuserInfoService.getById(wxuserInfoParam.getUserInfoId());
        WxuserInfoResult result = new WxuserInfoResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxuserInfoResult> list(@RequestBody(required = false) WxuserInfoParam wxuserInfoParam) {
        if(ToolUtil.isEmpty(wxuserInfoParam)){
            wxuserInfoParam = new WxuserInfoParam();
        }
        return this.wxuserInfoService.findPageBySpec(wxuserInfoParam);
    }




}


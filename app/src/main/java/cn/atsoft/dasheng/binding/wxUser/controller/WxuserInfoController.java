package cn.atsoft.dasheng.binding.wxUser.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


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
    @Permission
    public ResponseData addItem(@RequestBody WxuserInfoParam wxuserInfoParam) {
//        this.wxuserInfoService.add(wxuserInfoParam);
        Boolean aBoolean = wxuserInfoService.sendPermissions(4L, 1430422569372217346L);
        return ResponseData.success(aBoolean);
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
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
    @Permission
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
    @Permission
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
    @Permission
    public PageInfo<WxuserInfoResult> list(@RequestBody(required = false) WxuserInfoParam wxuserInfoParam) {
        if(ToolUtil.isEmpty(wxuserInfoParam)){
            wxuserInfoParam = new WxuserInfoParam();
        }
        return this.wxuserInfoService.findPageBySpec(wxuserInfoParam);
    }




}



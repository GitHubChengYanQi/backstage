package cn.atsoft.dasheng.miniapp.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.miniapp.entity.WxMaMessage;
import cn.atsoft.dasheng.miniapp.model.params.WxMaMessageParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaMessageResult;
import cn.atsoft.dasheng.miniapp.service.WxMaMessageService;
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
 * 小程序订阅消息控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-11 13:34:43
 */
@RestController
@RequestMapping("/wxMaMessage")
@Api(tags = "小程序订阅消息")
public class WxMaMessageController extends BaseController {

    @Autowired
    private WxMaMessageService wxMaMessageService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxMaMessageParam wxMaMessageParam) {
        this.wxMaMessageService.add(wxMaMessageParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxMaMessageParam wxMaMessageParam) {
//        WxMaMessage detail = this.wxMaMessageService.getById(wxMaMessageParam.get());
//        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
//            throw new ServiceException(500,"您无权操作该数据");
//        }
        this.wxMaMessageService.update(wxMaMessageParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxMaMessageParam wxMaMessageParam)  {
        this.wxMaMessageService.delete(wxMaMessageParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxMaMessageResult> detail(@RequestBody WxMaMessageParam wxMaMessageParam) {
        WxMaMessage detail = this.wxMaMessageService.getById(wxMaMessageParam.getMaMessageId());
//        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
//            throw new ServiceException(500,"您无权查看该数据");
//        }
        WxMaMessageResult result = new WxMaMessageResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxMaMessageResult> list(@RequestBody(required = false) WxMaMessageParam wxMaMessageParam) {
        if(ToolUtil.isEmpty(wxMaMessageParam)){
            wxMaMessageParam = new WxMaMessageParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.wxMaMessageService.findPageBySpec(wxMaMessageParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.wxMaMessageService.findPageBySpec(wxMaMessageParam,dataScope);
        }
//        return this.wxMaMessageService.findPageBySpec(wxMaMessageParam);
    }




}



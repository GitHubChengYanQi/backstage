package cn.atsoft.dasheng.miniapp.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.miniapp.entity.WxMaConfig;
import cn.atsoft.dasheng.miniapp.model.params.WxMaConfigParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaConfigResult;
import cn.atsoft.dasheng.miniapp.service.WxMaConfigService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.service.TenantService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 微信小程序配置表（对应租户）控制器
 *
 * @author Captain_Jazz
 * @Date 2023-04-25 09:53:02
 */
@RestController
@RequestMapping("/wxMaConfig")
@Api(tags = "微信小程序配置表（对应租户）")
public class WxMaConfigController extends BaseController {

    @Autowired
    private WxMaConfigService wxMaConfigService;
    @Autowired
    private TenantService tenantService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxMaConfigParam wxMaConfigParam) {
        //校验wxMaConfigParam.getTenantId()是否存在
        WxMaConfig wxMaConfig = wxMaConfigService.getById(wxMaConfigParam.getTenantId());
        if (wxMaConfig != null) {
            throw  new ServiceException(500, "租户ID已存在");
        }
        //判断wxMaConfigParam中的参数是否为null
        if (ToolUtil.isOneEmpty(wxMaConfigParam.getTenantId(), wxMaConfigParam.getAppid(), wxMaConfigParam.getAesKey(),wxMaConfigParam.getToken(), wxMaConfigParam.getMsgDataFormat(),wxMaConfigParam.getSecret())) {
            throw new ServiceException(500, "参数不能为空");
        }
        checkParam(wxMaConfigParam);


        this.wxMaConfigService.add(wxMaConfigParam);
        return ResponseData.success();
    }
    //校验操作权限
    public void checkParam(WxMaConfigParam wxMaConfigParam){
        //如果登录用户的tenantId不等于wxMaConfigParam.getTenantId()，则抛出异常
        Tenant tenant = tenantService.getById(wxMaConfigParam.getTenantId());
        if (tenant == null) {
            throw new ServiceException(500, "租户ID不存在");
        }
        //操作权限校验
        if (!LoginContextHolder.getContext().getUserId().equals(tenant.getCreateUser()) && !LoginContextHolder.getContext().isAdmin()) {
            throw new ServiceException(500, "您无权操作此租户");
        }
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxMaConfigParam wxMaConfigParam) {
        checkParam(wxMaConfigParam);
        //判断wxMaConfigParam中的参数是否为null
        if (ToolUtil.isOneEmpty(wxMaConfigParam.getTenantId(), wxMaConfigParam.getAppid(), wxMaConfigParam.getAesKey(),wxMaConfigParam.getToken(), wxMaConfigParam.getMsgDataFormat(),wxMaConfigParam.getSecret())) {
            throw new ServiceException(500, "参数不能为空");
        }
        this.wxMaConfigService.update(wxMaConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxMaConfigParam wxMaConfigParam)  {
        checkParam(wxMaConfigParam);
        this.wxMaConfigService.delete(wxMaConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxMaConfigResult> detail(@RequestBody WxMaConfigParam wxMaConfigParam) {
        WxMaConfig detail = this.wxMaConfigService.getById(wxMaConfigParam.getWxMaConfigId());
        WxMaConfigResult result = new WxMaConfigResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        this.wxMaConfigService.format(Collections.singletonList(result));
//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxMaConfigResult> list(@RequestBody(required = false) WxMaConfigParam wxMaConfigParam) {
        if(ToolUtil.isEmpty(wxMaConfigParam)){
            wxMaConfigParam = new WxMaConfigParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.wxMaConfigService.findPageBySpec(wxMaConfigParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.wxMaConfigService.findPageBySpec(wxMaConfigParam,dataScope);
        }
//        return this.wxMaConfigService.findPageBySpec(wxMaConfigParam);
    }




}



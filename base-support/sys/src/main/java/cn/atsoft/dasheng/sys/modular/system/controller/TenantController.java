package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import cn.atsoft.dasheng.sys.modular.system.service.TenantBindService;
import cn.atsoft.dasheng.sys.modular.system.service.TenantService;
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
import java.util.stream.Collectors;


/**
 * 系统租户表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-04-07 08:29:29
 */
@RestController
@RequestMapping("/tenant")
@Api(tags = "系统租户表")
public class TenantController extends BaseController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private TenantBindService tenantBindService;


    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TenantParam tenantParam) {
        return ResponseData.success(this.tenantService.add(tenantParam));
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TenantParam tenantParam) {
        if (LoginContextHolder.getContext().isAdmin() || LoginContextHolder.getContext().getTenantId().equals(tenantParam.getTenantId())) {
            Tenant tenant = this.tenantService.getById(tenantParam.getTenantId());
            if (!tenant.getCreateUser().equals(LoginContextHolder.getContext().getUserId())) {
                throw new ServiceException(500, "非管理员或租户创建者不能编辑租户");
            }
            this.tenantService.update(tenantParam);
            return ResponseData.success();
        }
        throw new ServiceException(500, "非管理员或租户创建者不能编辑租户");
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody TenantParam tenantParam) {
//        this.tenantService.delete(tenantParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TenantResult> detail(@RequestBody TenantParam tenantParam) {
        Tenant detail = this.tenantService.getById(tenantParam.getTenantId());

        TenantResult result = new TenantResult();
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
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TenantResult> list(@RequestBody(required = false) TenantParam tenantParam) {
        if (ToolUtil.isEmpty(tenantParam)) {
            tenantParam = new TenantParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.tenantService.findPageBySpec(tenantParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(), LoginContextHolder.getContext().getTenantId());
            return this.tenantService.findPageBySpec(tenantParam, dataScope);
        }
//        return this.tenantService.findPageBySpec(tenantParam);
    }

    /**
     * 更换租户
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/changeTenant", method = RequestMethod.POST)
    @ApiOperation("更换租户")
    public ResponseData changeTenant(@RequestBody(required = false) TenantParam tenantParam) {
        return ResponseData.success(this.tenantService.changeTenant(tenantParam));
    }

    /**
     * 更换租户
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    @RequestMapping(value = "/getMyTenants", method = RequestMethod.POST)
    @ApiOperation("更换租户")
    public ResponseData getMyTenants(@RequestBody(required = false) TenantParam tenantParam) {
        List<Long> tenantIds = tenantBindService.lambdaQuery().eq(TenantBind::getUserId, LoginContextHolder.getContext().getUserId()).eq(TenantBind::getDisplay, 1).eq(TenantBind::getStatus, 99).list().stream().map(TenantBind::getTenantId).distinct().collect(Collectors.toList());
        if (tenantIds.size() == 0) {
            return ResponseData.success(new ArrayList<>());
        }
        tenantParam.setTenantIds(tenantIds);
        return ResponseData.success(this.tenantService.findListBySpec(tenantParam));
    }


}



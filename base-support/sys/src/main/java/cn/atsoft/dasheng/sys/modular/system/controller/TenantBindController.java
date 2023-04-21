package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindResult;
import cn.atsoft.dasheng.sys.modular.system.service.TenantBindService;
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
 * 租户用户绑定表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-04-19 10:39:25
 */
@RestController
@RequestMapping("/tenantBind")
@Api(tags = "租户用户绑定表")
public class TenantBindController extends BaseController {

    @Autowired
    private TenantBindService tenantBindService;

    @Autowired
    private AuthService authService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TenantBindParam tenantBindParam) {
        this.tenantBindService.add(tenantBindParam);
        return ResponseData.success();
    }

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/shareAdd", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData shareAdd(@RequestBody TenantBindParam tenantBindParam) {
        tenantBindParam.setUserId(LoginContextHolder.getContext().getUserId());
        this.tenantBindService.add(tenantBindParam);
        return ResponseData.success();
    }

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/getStatus", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData getStatus(@RequestBody TenantBindParam tenantBindParam) {
        TenantBind one = this.tenantBindService.lambdaQuery().eq(TenantBind::getTenantId, tenantBindParam.getTenantId()).eq(TenantBind::getUserId, LoginContextHolder.getContext().getUserId()).one();
        return ResponseData.success(ToolUtil.isNotEmpty(one) ? one.getStatus() : null);
    }

    /**
     * 更新状态
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData updateStatus(@RequestBody TenantBindParam tenantBindParam) {
        //主键为空抛出异常
        if (ToolUtil.isEmpty(tenantBindParam.getTenantBindId()) || ToolUtil.isEmpty(tenantBindParam.getStatus())) {
            throw new RuntimeException("参数错误");
        }
        this.tenantBindService.updateById(new TenantBind() {{
            setTenantBindId(tenantBindParam.getTenantBindId());
            setStatus(tenantBindParam.getStatus());
        }});

        return ResponseData.success(authService.login(LoginContextHolder.getContext().getUser().getAccount()));
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TenantBindParam tenantBindParam) {

        this.tenantBindService.update(tenantBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TenantBindParam tenantBindParam) {
        this.tenantBindService.delete(tenantBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TenantBindResult> detail(@RequestBody TenantBindParam tenantBindParam) {
        TenantBind detail = this.tenantBindService.getById(tenantBindParam.getTenantId());
        TenantBindResult result = new TenantBindResult();
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
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TenantBindResult> list(@RequestBody(required = false) TenantBindParam tenantBindParam) {
        if (ToolUtil.isEmpty(tenantBindParam)) {
            tenantBindParam = new TenantBindParam();
        }
//        if (LoginContextHolder.getContext().isAdmin()) {
        return this.tenantBindService.findPageBySpec(tenantBindParam, null);
//        } else {
//             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
//             return  this.tenantBindService.findPageBySpec(tenantBindParam,dataScope);
//        }
//        return this.tenantBindService.findPageBySpec(tenantBindParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/noPageList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData noPageList(@RequestBody(required = false) TenantBindParam tenantBindParam) {
        if (ToolUtil.isEmpty(tenantBindParam)) {
            tenantBindParam = new TenantBindParam();
        }
//        if (LoginContextHolder.getContext().isAdmin()) {
        return ResponseData.success(this.tenantBindService.findListBySpec(tenantBindParam));
//        } else {
//             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
//             return  this.tenantBindService.findPageBySpec(tenantBindParam,dataScope);
//        }
//        return this.tenantBindService.findPageBySpec(tenantBindParam);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/batchUpdateStatus", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData batchUpdateStatus(@RequestBody(required = false) TenantBindParam tenantBindParam) {
        //防止使用参数为空
        if (ToolUtil.isEmpty(tenantBindParam) || ToolUtil.isEmpty(tenantBindParam.getTenantBindIds())) {
            throw new RuntimeException("参数错误");
        }
        //通过tenantBindParam.tenantBindId集合
        this.tenantBindService.lambdaUpdate().in(TenantBind::getTenantBindId, tenantBindParam.getTenantBindIds()).set(TenantBind::getStatus,99).update();
        return ResponseData.success();
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    @RequestMapping(value = "/getBindIdsByTenantId", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData batchUpdateStatus(Long tenantId) {
        //通过tenantId查询tenantBindId集合
        List<TenantBind> tenantBinds = this.tenantBindService.lambdaQuery().eq(TenantBind::getTenantId, tenantId).list();
        return ResponseData.success(tenantBinds.stream().map(TenantBind::getTenantBindId).collect(Collectors.toList()));
    }


}



package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantInviteLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantInviteLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantInviteLogResult;
import cn.atsoft.dasheng.sys.modular.system.service.DeptService;
import cn.atsoft.dasheng.sys.modular.system.service.TenantInviteLogService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.service.TenantService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 邀请记录申请记录控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-12 09:38:43
 */
@RestController
@RequestMapping("/tenantInviteLog")
@Api(tags = "邀请记录申请记录")
public class TenantInviteLogController extends BaseController {

    @Autowired
    private TenantInviteLogService tenantInviteLogService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService userService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TenantInviteLogParam tenantInviteLogParam) {
        return ResponseData.success(this.tenantInviteLogService.add(tenantInviteLogParam));
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TenantInviteLogParam tenantInviteLogParam) {
        TenantInviteLog detail = this.tenantInviteLogService.getById(tenantInviteLogParam.getTenantInviteLogId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权操作该数据");
        }
        this.tenantInviteLogService.update(tenantInviteLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TenantInviteLogParam tenantInviteLogParam)  {
        this.tenantInviteLogService.delete(tenantInviteLogParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TenantInviteLogResult> detail(@RequestBody TenantInviteLogParam tenantInviteLogParam) {
        TenantInviteLog detail = this.tenantInviteLogService.getById(tenantInviteLogParam.getTenantInviteLogId());
//        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
//            throw new ServiceException(500,"您无权查看该数据");
//        }
        TenantInviteLogResult result = new TenantInviteLogResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        result.setTenantResult(tenantService.getTenantResultsByIds(Collections.singletonList(result.getTenantId())).get(0));
        result.setDept(deptService.getById(result.getDeptId()));
        if (ToolUtil.isNotEmpty(result.getInviterUser())) {
            result.setUserResult(userService.getUserResultsByIds(Collections.singletonList(result.getInviterUser())).get(0));

        }
//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TenantInviteLogResult> list(@RequestBody(required = false) TenantInviteLogParam tenantInviteLogParam) {
        if(ToolUtil.isEmpty(tenantInviteLogParam)){
            tenantInviteLogParam = new TenantInviteLogParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.tenantInviteLogService.findPageBySpec(tenantInviteLogParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.tenantInviteLogService.findPageBySpec(tenantInviteLogParam,dataScope);
        }
//        return this.tenantInviteLogService.findPageBySpec(tenantInviteLogParam);
    }




}



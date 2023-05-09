package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantOperationLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantOperationLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantOperationLogResult;
import cn.atsoft.dasheng.sys.modular.system.service.TenantOperationLogService;
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
 * 部门造作记录表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-09 14:42:05
 */
@RestController
@RequestMapping("/tenantOperationLog")
@Api(tags = "部门造作记录表")
public class TenantOperationLogController extends BaseController {

    @Autowired
    private TenantOperationLogService tenantOperationLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TenantOperationLogParam tenantOperationLogParam) {
        this.tenantOperationLogService.add(tenantOperationLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TenantOperationLogParam tenantOperationLogParam) {
        TenantOperationLog detail = this.tenantOperationLogService.getById(tenantOperationLogParam.getTenantOperationLogId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权操作该数据");
        }
        this.tenantOperationLogService.update(tenantOperationLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TenantOperationLogParam tenantOperationLogParam)  {
        this.tenantOperationLogService.delete(tenantOperationLogParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TenantOperationLogResult> detail(@RequestBody TenantOperationLogParam tenantOperationLogParam) {
        TenantOperationLog detail = this.tenantOperationLogService.getById(tenantOperationLogParam.getTenantOperationLogId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权查看该数据");
        }
        TenantOperationLogResult result = new TenantOperationLogResult();
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
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TenantOperationLogResult> list(@RequestBody(required = false) TenantOperationLogParam tenantOperationLogParam) {
        if(ToolUtil.isEmpty(tenantOperationLogParam)){
            tenantOperationLogParam = new TenantOperationLogParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.tenantOperationLogService.findPageBySpec(tenantOperationLogParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.tenantOperationLogService.findPageBySpec(tenantOperationLogParam,dataScope);
        }
//        return this.tenantOperationLogService.findPageBySpec(tenantOperationLogParam);
    }




}



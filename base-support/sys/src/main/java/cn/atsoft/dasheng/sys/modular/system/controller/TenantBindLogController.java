package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBindLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindLogResult;
import cn.atsoft.dasheng.sys.modular.system.service.TenantBindLogService;
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
 * 邀请记录申请记录控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-09 14:42:05
 */
@RestController
@RequestMapping("/tenantBindLog")
@Api(tags = "邀请记录申请记录")
public class TenantBindLogController extends BaseController {

    @Autowired
    private TenantBindLogService tenantBindLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TenantBindLogParam tenantBindLogParam) {
        return ResponseData.success(this.tenantBindLogService.add(tenantBindLogParam));
    }

    /**
     * 更新状态
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData updateStatus(@RequestBody TenantBindLogParam tenantBindLogParam) {
        this.tenantBindLogService.updateStatus(tenantBindLogParam.getTenantBindLogId(), tenantBindLogParam.getStatus());
        return ResponseData.success();
    }
    /**
     * 更新状态
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/statusCount", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData statusCount(@RequestBody TenantBindLogParam tenantBindLogParam) {
        return ResponseData.success(tenantBindLogService.lambdaQuery().eq(TenantBindLog::getDisplay,1).eq(TenantBindLog::getStatus,1).eq(TenantBindLog::getType,"申请").eq(TenantBindLog::getTenantId,tenantBindLogParam.getTenantId()).count());
    }

    /**
     * 更新状态
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/getStatus", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData getStatus(@RequestBody TenantBindLogParam tenantBindLogParam) {
        TenantBindLog one = this.tenantBindLogService.lambdaQuery().eq(TenantBindLog::getTenantId, tenantBindLogParam.getTenantId()).eq(TenantBindLog::getType,"申请").eq(TenantBindLog::getUserId, LoginContextHolder.getContext().getUserId()).orderByDesc(TenantBindLog::getCreateTime).last(" limit 1" ).one();
        return ResponseData.success(ToolUtil.isNotEmpty(one) ? one.getStatus() : null);
    }

    /**
     * 更新状态
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/addLog", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addLog(@RequestBody TenantBindLogParam tenantBindLogParam) {
        Long logId = this.tenantBindLogService.addLog(tenantBindLogParam.getUserId(), tenantBindLogParam.getTenantId(), ToolUtil.isEmpty(tenantBindLogParam.getInviteDeptId()) ? null : tenantBindLogParam.getInviteDeptId(), tenantBindLogParam.getType());
        return ResponseData.success(logId);
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TenantBindLogParam tenantBindLogParam) {
        TenantBindLog detail = this.tenantBindLogService.getById(tenantBindLogParam.getTenantBindLogId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())) {
            throw new ServiceException(500, "您无权操作该数据");
        }
        this.tenantBindLogService.update(tenantBindLogParam);
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
    public ResponseData delete(@RequestBody TenantBindLogParam tenantBindLogParam) {
        this.tenantBindLogService.delete(tenantBindLogParam);
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
    public ResponseData<TenantBindLogResult> detail(@RequestBody TenantBindLogParam tenantBindLogParam) {
        TenantBindLog detail = this.tenantBindLogService.getById(tenantBindLogParam.getTenantBindLogId());
//        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())) {
//            throw new ServiceException(500, "您无权查看该数据");
//        }
        TenantBindLogResult result = new TenantBindLogResult();
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
    public PageInfo<TenantBindLogResult> list(@RequestBody(required = false) TenantBindLogParam tenantBindLogParam) {
        if (ToolUtil.isEmpty(tenantBindLogParam)) {
            tenantBindLogParam = new TenantBindLogParam();
        }
//        if (LoginContextHolder.getContext().isAdmin()) {
            return this.tenantBindLogService.findPageBySpec(tenantBindLogParam, null);
//        } else {
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(), LoginContextHolder.getContext().getTenantId());
//            return this.tenantBindLogService.findPageBySpec(tenantBindLogParam, dataScope);
//        }
//        return this.tenantBindLogService.findPageBySpec(tenantBindLogParam);
    }


}



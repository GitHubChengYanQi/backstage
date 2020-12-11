package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.SysLoginLogResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysLoginLog;
import cn.atsoft.dasheng.app.model.params.SysLoginLogParam;
import cn.atsoft.dasheng.app.service.SysLoginLogService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 登录记录控制器
 *
 * @author sing
 * @Date 2020-12-09 15:30:08
 */
@RestController
@RequestMapping("/sysLoginLog")
@Api(tags = "登录记录")
public class SysLoginLogController extends BaseController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    /**
     * 新增接口
     *
     * @author sing
     * @Date 2020-12-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SysLoginLogParam sysLoginLogParam) {
        this.sysLoginLogService.add(sysLoginLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author sing
     * @Date 2020-12-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SysLoginLogParam sysLoginLogParam) {
        this.sysLoginLogService.update(sysLoginLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author sing
     * @Date 2020-12-09
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SysLoginLogParam sysLoginLogParam)  {
        this.sysLoginLogService.delete(sysLoginLogParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author sing
     * @Date 2020-12-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SysLoginLog> detail(@RequestBody SysLoginLogParam sysLoginLogParam) {
        SysLoginLog detail = this.sysLoginLogService.getById(sysLoginLogParam.getLoginLogId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author sing
     * @Date 2020-12-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SysLoginLogResult> list(@RequestBody(required = false) SysLoginLogParam sysLoginLogParam) {
        return this.sysLoginLogService.findPageBySpec(sysLoginLogParam);
    }

}



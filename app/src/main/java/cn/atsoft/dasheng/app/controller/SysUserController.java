package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysUser;
import cn.atsoft.dasheng.app.model.params.SysUserParam;
import cn.atsoft.dasheng.app.model.result.SysUserResult;
import cn.atsoft.dasheng.app.service.SysUserService;
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
 * 管理员表控制器
 *
 * @author 
 * @Date 2021-08-06 09:01:41
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "管理员表")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SysUserParam sysUserParam) {
        this.sysUserService.add(sysUserParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-06
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SysUserParam sysUserParam) {

        this.sysUserService.update(sysUserParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-06
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SysUserParam sysUserParam)  {
        this.sysUserService.delete(sysUserParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SysUserResult> detail(@RequestBody SysUserParam sysUserParam) {
        SysUser detail = this.sysUserService.getById(sysUserParam.getUserId());
        SysUserResult result = new SysUserResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SysUserResult> list(@RequestBody(required = false) SysUserParam sysUserParam) {
        if(ToolUtil.isEmpty(sysUserParam)){
            sysUserParam = new SysUserParam();
        }
        return this.sysUserService.findPageBySpec(sysUserParam);
    }




}



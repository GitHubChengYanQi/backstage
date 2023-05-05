package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.system.entity.MenuConfig;
import cn.atsoft.dasheng.sys.modular.system.model.params.MenuConfigParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.MenuConfigResult;
import cn.atsoft.dasheng.sys.modular.system.service.MenuConfigService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 菜单显示配置表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-04-28 10:56:30
 */
@RestController
@RequestMapping("/menuConfig")
@Api(tags = "菜单显示配置表")
public class MenuConfigController extends BaseController {

    @Autowired
    private MenuConfigService menuConfigService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MenuConfigParam menuConfigParam) {
        this.menuConfigService.add(menuConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MenuConfigParam menuConfigParam) {

        this.menuConfigService.update(menuConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MenuConfigParam menuConfigParam)  {
        this.menuConfigService.delete(menuConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MenuConfigResult> detail(@RequestBody MenuConfigParam menuConfigParam) {
        MenuConfig detail = this.menuConfigService.getById(menuConfigParam.getConfigId());
        MenuConfigResult result = new MenuConfigResult();
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
     * @Date 2023-04-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MenuConfigResult> list(@RequestBody(required = false) MenuConfigParam menuConfigParam) {
        if(ToolUtil.isEmpty(menuConfigParam)){
            menuConfigParam = new MenuConfigParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.menuConfigService.findPageBySpec(menuConfigParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.menuConfigService.findPageBySpec(menuConfigParam,dataScope);
        }
//        return this.menuConfigService.findPageBySpec(menuConfigParam);
    }




}



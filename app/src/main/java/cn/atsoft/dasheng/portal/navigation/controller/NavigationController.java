package cn.atsoft.dasheng.portal.navigation.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.navigation.entity.Navigation;
import cn.atsoft.dasheng.portal.navigation.model.params.NavigationParam;
import cn.atsoft.dasheng.portal.navigation.model.result.NavigationRequest;
import cn.atsoft.dasheng.portal.navigation.model.result.NavigationResult;
import cn.atsoft.dasheng.portal.navigation.service.NavigationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 导航表控制器
 *
 * @author
 * @Date 2021-08-18 08:40:30
 */
@RestController
@RequestMapping("/navigation")
@Api(tags = "导航表")
public class NavigationController extends BaseController {

    @Autowired
    private NavigationService navigationService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody NavigationParam navigationParam) {
        this.navigationService.add(navigationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody NavigationParam navigationParam) {

        this.navigationService.update(navigationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody NavigationParam navigationParam) {
        this.navigationService.delete(navigationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody NavigationParam navigationParam) {
        Navigation detail = this.navigationService.getById(navigationParam.getNavigationId());
        NavigationResult result = new NavigationResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<NavigationResult> list(@RequestBody(required = false) NavigationParam navigationParam) {
        if (ToolUtil.isEmpty(navigationParam)) {
            navigationParam = new NavigationParam();
        }
        return this.navigationService.findPageBySpec(navigationParam);
    }

    @Permission
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    public ResponseData batchDelete(@RequestBody NavigationRequest navigationRequest) {
        navigationService.batchDelete(navigationRequest.getNavigationId());
        return ResponseData.success();
    }

}



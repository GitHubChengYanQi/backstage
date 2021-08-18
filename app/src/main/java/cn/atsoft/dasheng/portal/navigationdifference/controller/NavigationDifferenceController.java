package cn.atsoft.dasheng.portal.navigationdifference.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.navigationdifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationdifference.model.params.NavigationDifferenceParam;
import cn.atsoft.dasheng.portal.navigationdifference.model.result.NavigationDifferenceResult;
import cn.atsoft.dasheng.portal.navigationdifference.service.NavigationDifferenceService;
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
 * 导航分类控制器
 *
 * @author 
 * @Date 2021-08-18 10:43:16
 */
@RestController
@RequestMapping("/navigationDifference")
@Api(tags = "导航分类")
public class NavigationDifferenceController extends BaseController {

    @Autowired
    private NavigationDifferenceService navigationDifferenceService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody NavigationDifferenceParam navigationDifferenceParam) {
        this.navigationDifferenceService.add(navigationDifferenceParam);
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
    public ResponseData update(@RequestBody NavigationDifferenceParam navigationDifferenceParam) {

        this.navigationDifferenceService.update(navigationDifferenceParam);
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
    public ResponseData delete(@RequestBody NavigationDifferenceParam navigationDifferenceParam)  {
        this.navigationDifferenceService.delete(navigationDifferenceParam);
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
    public ResponseData<NavigationDifferenceResult> detail(@RequestBody NavigationDifferenceParam navigationDifferenceParam) {
        NavigationDifference detail = this.navigationDifferenceService.getById(navigationDifferenceParam.getClassificationId());
        NavigationDifferenceResult result = new NavigationDifferenceResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
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
    public PageInfo<NavigationDifferenceResult> list(@RequestBody(required = false) NavigationDifferenceParam navigationDifferenceParam) {
        if(ToolUtil.isEmpty(navigationDifferenceParam)){
            navigationDifferenceParam = new NavigationDifferenceParam();
        }
        return this.navigationDifferenceService.findPageBySpec(navigationDifferenceParam);
    }




}



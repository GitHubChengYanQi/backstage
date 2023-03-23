package cn.atsoft.dasheng.portal.navigationDifference.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.navigation.wrapper.NavigationSelectWrapper;
import cn.atsoft.dasheng.portal.navigationDifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationDifference.model.params.NavigationDifferenceParam;
import cn.atsoft.dasheng.portal.navigationDifference.model.result.NavigationDifferenceResult;
import cn.atsoft.dasheng.portal.navigationDifference.service.NavigationDifferenceService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
    public ResponseData detail(@RequestBody NavigationDifferenceParam navigationDifferenceParam) {
        NavigationDifference detail = this.navigationDifferenceService.getById(navigationDifferenceParam.getClassificationId());
        NavigationDifferenceResult result = new NavigationDifferenceResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<NavigationDifference>  navigationDifferenceQueryWrapper= new QueryWrapper<>();
        navigationDifferenceQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.navigationDifferenceService.listMaps(navigationDifferenceQueryWrapper);
        NavigationSelectWrapper navigationSelectWrapper = new NavigationSelectWrapper(list);
        List<Map<String, Object>> result = navigationSelectWrapper.wrap();
        return ResponseData.success(result);

    }




}



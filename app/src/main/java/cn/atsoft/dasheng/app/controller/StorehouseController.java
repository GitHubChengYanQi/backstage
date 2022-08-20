package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.params.StorehouseParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.StorehouseSelectWrapper;

import java.util.List;
import java.util.Map;


/**
 * 地点表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/storehouse")
@Api(tags = "地点表")
public class StorehouseController extends BaseController {

    @Autowired
    private StorehouseService storehouseService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StorehouseParam storehouseParam) {
        Long add = this.storehouseService.add(storehouseParam);
        if (storehouseParam.getCapacity() == null && storehouseParam.getMeasure() == null) {
            return ResponseData.success(add);
        } else {
            if (storehouseParam.getCapacity() > 0 && storehouseParam.getMeasure() > 0) {
                return ResponseData.success(add);
            } else {
                return ResponseData.error("请输入正确值");
            }
        }

    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehouseParam storehouseParam) {

        this.storehouseService.update(storehouseParam);
        if (storehouseParam.getCapacity() == null && storehouseParam.getMeasure() == null) {
            return ResponseData.success();
        } else {
            if (storehouseParam.getCapacity() > 0 && storehouseParam.getMeasure() > 0) {
                return ResponseData.success();
            } else {
                return ResponseData.error("请输入正确值");
            }
        }
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehouseParam storehouseParam) {
        this.storehouseService.delete(storehouseParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody StorehouseParam storehouseParam) {
        Storehouse detail = this.storehouseService.getById(storehouseParam.getStorehouseId());
        StorehouseResult result = new StorehouseResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StorehouseResult> list(@RequestBody(required = false) StorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam)) {
            storehouseParam = new StorehouseParam();
        }
//        return this.storehouseService.findPageBySpec(storehouseParam);

        return this.storehouseService.findPageBySpec(storehouseParam, null);


    }

    /**
     * 选择列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) StorehouseParam storehouseParam) {
        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        storehouseQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(storehouseParam) && ToolUtil.isNotEmpty(storehouseParam.getName())) {
            storehouseQueryWrapper.like("name", storehouseParam.getName());
        }
        List<Map<String, Object>> list = this.storehouseService.listMaps(storehouseQueryWrapper);
        StorehouseSelectWrapper factory = new StorehouseSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}



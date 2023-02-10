package cn.atsoft.dasheng.storehouse.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.atsoft.dasheng.storehouse.wrapper.RestStorehouseSelectWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 地点表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/storehouse/{version}")
@Api(tags = "仓库")
@ApiVersion("2.0")
public class RestStorehouseController extends BaseController {

    @Autowired
    private RestStorehouseService storehouseService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestStorehouseParam storehouseParam) {
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
    public ResponseData update(@RequestBody RestStorehouseParam storehouseParam) {

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
    public ResponseData delete(@RequestBody RestStorehouseParam storehouseParam) {
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
    public ResponseData detail(@RequestBody RestStorehouseParam storehouseParam) {
        RestStorehouse detail = this.storehouseService.getById(storehouseParam.getStorehouseId());
        RestStorehouseResult result = new RestStorehouseResult();
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
    public PageInfo list(@RequestBody(required = false) RestStorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam)) {
            storehouseParam = new RestStorehouseParam();
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
    public ResponseData listSelect(@RequestBody(required = false) RestStorehouseParam storehouseParam) {
        QueryWrapper<RestStorehouse> storehouseQueryWrapper = new QueryWrapper<>();
        storehouseQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(storehouseParam) && ToolUtil.isNotEmpty(storehouseParam.getName())) {
            storehouseQueryWrapper.like("name", storehouseParam.getName());
        }
        List<Map<String, Object>> list = this.storehouseService.listMaps(storehouseQueryWrapper);
        RestStorehouseSelectWrapper factory = new RestStorehouseSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}



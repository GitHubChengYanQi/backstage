package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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
import cn.hutool.core.bean.BeanUtil;
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

    @Autowired
    private StockDetailsService stockDetailsService;

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

        Storehouse update = this.storehouseService.update(storehouseParam);
        StorehouseParam result = BeanUtil.copyProperties(update, StorehouseParam.class);
        if (storehouseParam.getCapacity() == null && storehouseParam.getMeasure() == null) {
            return ResponseData.success(result);
        } else {
            if (storehouseParam.getCapacity() > 0 && storehouseParam.getMeasure() > 0) {
                return ResponseData.success(result);
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
     * 删除接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData deleteBatch(@RequestBody StorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam.getStorehouseIds())) {
            return ResponseData.error("参数错误");
        }
        List<Storehouse> storehouses = storehouseService.listByIds(storehouseParam.getStorehouseIds());
        if (storehouses.size()!= storehouseParam.getStorehouseIds().size()){
            return ResponseData.error("参数错误");
        }
        List<StockDetails> stockDetails = stockDetailsService.lambdaQuery().in(StockDetails::getStorehouseId, storehouseParam.getStorehouseIds()).eq(StockDetails::getDisplay, 1).eq(StockDetails::getStage, 1).groupBy(StockDetails::getStorehouseId).list();

        for (Storehouse storehouse : storehouses) {
            if (stockDetails.stream().anyMatch(i->i.getStorehouseId().equals(storehouse.getStorehouseId()))){
                return ResponseData.error("该仓库有库存，不能删除");
            }
            storehouse.setDisplay(0);
        }
        this.storehouseService.updateBatchById(storehouses);
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
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo list(@RequestBody(required = false) StorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam)) {
            storehouseParam = new StorehouseParam();
        }
//        return this.storehouseService.findPageBySpec(storehouseParam);

        return this.storehouseService.findPageBySpec(storehouseParam, new DataScope(null,LoginContextHolder.getContext().getTenantId()));


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
        storehouseQueryWrapper.eq("tenant_id", LoginContextHolder.getContext().getTenantId());
        if (ToolUtil.isNotEmpty(storehouseParam) && ToolUtil.isNotEmpty(storehouseParam.getName())) {
            storehouseQueryWrapper.like("name", storehouseParam.getName());
        }
        List<Map<String, Object>> list = this.storehouseService.listMaps(storehouseQueryWrapper);
        StorehouseSelectWrapper factory = new StorehouseSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}



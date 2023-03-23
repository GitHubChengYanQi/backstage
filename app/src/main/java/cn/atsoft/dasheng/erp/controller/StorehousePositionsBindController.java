package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 库位绑定物料表控制器
 *
 * @author song
 * @Date 2022-01-20 11:15:05
 */
@RestController
@RequestMapping("/storehousePositionsBind")
@Api(tags = "库位绑定物料表")
public class StorehousePositionsBindController extends BaseController {

    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@Valid @RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
        StorehousePositionsBind bind = this.storehousePositionsBindService.add(storehousePositionsBindParam);
        return ResponseData.success(bind);
    }
    /**
     * 批量新增接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/addBatch", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addBatch(@Valid @RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
        this.storehousePositionsBindService.bindBatch(storehousePositionsBindParam);
        return ResponseData.success();
    }


//    @RequestMapping(value = "/SpuAddBind", method = RequestMethod.POST)
//    public ResponseData SpuAddBind(@Valid @RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
//        this.storehousePositionsBindService.SpuAddBind(storehousePositionsBindParam);
//        return ResponseData.success();
//    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {

        this.storehousePositionsBindService.update(storehousePositionsBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
        this.storehousePositionsBindService.delete(storehousePositionsBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData treeView(@RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
        List<StorehousePositionsResult> results = this.storehousePositionsBindService.treeView(storehousePositionsBindParam.getSkuIds());
        return ResponseData.success(results);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {
        StorehousePositionsBind detail = this.storehousePositionsBindService.getById(storehousePositionsBindParam.getBindId());
        StorehousePositionsBindResult result = new StorehousePositionsBindResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 通过sku获取库位
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/getPositions", method = RequestMethod.POST)
    @ApiOperation("详情")
    public List<StorehousePositionsResult> getPositions(@RequestBody StorehousePositionsBindParam storehousePositionsBindParam) {

        if (ToolUtil.isNotEmpty(storehousePositionsBindParam.getSkuId())) {
            return storehousePositionsBindService.sku2position(storehousePositionsBindParam.getSkuId());
        }


        return null;
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-01-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StorehousePositionsBindResult> list(@RequestBody(required = false) StorehousePositionsBindParam storehousePositionsBindParam) {
        if (ToolUtil.isEmpty(storehousePositionsBindParam)) {
            storehousePositionsBindParam = new StorehousePositionsBindParam();
        }
        return this.storehousePositionsBindService.findPageBySpec(storehousePositionsBindParam);
    }


}



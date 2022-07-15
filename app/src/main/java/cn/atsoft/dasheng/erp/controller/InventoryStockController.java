package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.model.params.InventoryStockParam;
import cn.atsoft.dasheng.erp.model.result.InventoryStockResult;
import cn.atsoft.dasheng.erp.service.InventoryStockService;
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
 * 库存盘点处理控制器
 *
 * @author song
 * @Date 2022-07-15 14:47:06
 */
@RestController
@RequestMapping("/inventoryStock")
@Api(tags = "库存盘点处理")
public class InventoryStockController extends BaseController {

    @Autowired
    private InventoryStockService inventoryStockService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-07-15
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody InventoryStockParam inventoryStockParam) {
//        this.inventoryStockService.add(inventoryStockParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-07-15
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InventoryStockParam inventoryStockParam) {
//
//        this.inventoryStockService.update(inventoryStockParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-07-15
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InventoryStockParam inventoryStockParam)  {
//        this.inventoryStockService.delete(inventoryStockParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author song
//     * @Date 2022-07-15
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<InventoryStockResult> detail(@RequestBody InventoryStockParam inventoryStockParam) {
//        InventoryStock detail = this.inventoryStockService.getById(inventoryStockParam.getInventoryStockId());
//        InventoryStockResult result = new InventoryStockResult();
//        ToolUtil.copyProperties(detail, result);
//
//        result.setValue(parentValue);
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author song
//     * @Date 2022-07-15
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<InventoryStockResult> list(@RequestBody(required = false) InventoryStockParam inventoryStockParam) {
//        if(ToolUtil.isEmpty(inventoryStockParam)){
//            inventoryStockParam = new InventoryStockParam();
//        }
//        return this.inventoryStockService.findPageBySpec(inventoryStockParam);
//    }
//
//


}



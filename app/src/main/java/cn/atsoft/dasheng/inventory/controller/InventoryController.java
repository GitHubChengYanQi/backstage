package cn.atsoft.dasheng.inventory.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.inventory.entity.Inventory;
import cn.atsoft.dasheng.inventory.model.params.InventoryParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryResult;
import cn.atsoft.dasheng.inventory.pojo.InventoryRequest;
import cn.atsoft.dasheng.inventory.service.InventoryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.hutool.core.convert.Convert;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 盘点任务主表控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-27 09:27:27
 */
@RestController
@RequestMapping("/inventory")
@Api(tags = "盘点任务主表")
public class InventoryController extends BaseController {
    @Autowired
    private OrCodeBindService bindService;

    @Autowired
    private InventoryService inventoryService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.add(inventoryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InventoryParam inventoryParam) {

        this.inventoryService.update(inventoryParam);
        return ResponseData.success();
    }


    /**
     * 盘点
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    @ApiOperation("盘点")
    public ResponseData inventory(@RequestBody InventoryRequest inventoryRequest) {
        this.inventoryService.inventory(inventoryRequest);
        return ResponseData.success();
    }

    /**
     * 扫码 盘点
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/inventoryByCodeId", method = RequestMethod.GET)
    @ApiOperation("盘点")
    public ResponseData inventory(@Param("id") Long id) {
        OrCodeBind codeBind = bindService.query().eq("qr_code_id", id).one();
        if (ToolUtil.isEmpty(codeBind)) {
            throw new ServiceException(500, "请扫描正确二维码");
        }
        switch (codeBind.getSource()) {
            case "item":
                InkindResult inkindResult = this.inventoryService.inkindInventory(codeBind.getFormId());
                return ResponseData.success(inkindResult);
            case "storehousePositions":
                StorehousePositionsResult positionsResult = this.inventoryService.positionInventory(codeBind.getFormId());
                return ResponseData.success(positionsResult);
            default:
                throw new ServiceException(500, "请扫描正确二维码");
        }

    }


    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.delete(inventoryParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InventoryResult> detail(@RequestBody InventoryParam inventoryParam) {
        Inventory detail = this.inventoryService.getById(inventoryParam.getInventoryTaskId());
        InventoryResult result = new InventoryResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InventoryResult> list(@RequestBody(required = false) InventoryParam inventoryParam) {
        if (ToolUtil.isEmpty(inventoryParam)) {
            inventoryParam = new InventoryParam();
        }
        return this.inventoryService.findPageBySpec(inventoryParam);
    }


}



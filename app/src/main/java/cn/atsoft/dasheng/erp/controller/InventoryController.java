package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


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



    @RequestMapping(value = "/selectCondition", method = RequestMethod.POST)
    @ApiOperation("条件盘点")
    public ResponseData selectCondition(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.selectCondition(inventoryParam);
        return ResponseData.success();
    }

//
//    /**
//     * 编辑接口
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-27
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InventoryParam inventoryParam) {
//
//        this.inventoryService.update(inventoryParam);
//        return ResponseData.success();
//    }


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
        InventoryDetailResult inventoryDetailResult = new InventoryDetailResult();
        switch (codeBind.getSource()) {
            case "item":
                InkindResult inkindResult = this.inventoryService.inkindInventory(codeBind.getFormId());
                inventoryDetailResult.setType("inkind");
                inventoryDetailResult.setObject(inkindResult);
                return ResponseData.success(inventoryDetailResult);
            case "storehousePositions":
                StorehousePositionsResult positionsResult = this.inventoryService.positionInventory(codeBind.getFormId());
                inventoryDetailResult.setType("positions");
                inventoryDetailResult.setObject(positionsResult);
                return ResponseData.success(inventoryDetailResult);
            default:
                throw new ServiceException(500, "请扫描正确二维码");
        }

    }


//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-27
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InventoryParam inventoryParam) {
//        this.inventoryService.delete(inventoryParam);
//        return ResponseData.success();
//    }

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



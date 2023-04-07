package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.hutool.core.bean.BeanUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


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

    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private AnomalyOrderService anomalyOrderService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.bySku(inventoryParam);  //通过物料筛选出 品牌 库位
        Inventory inventory = this.inventoryService.add(inventoryParam);
        return ResponseData.success(inventory);
    }


    @RequestMapping(value = "/InventoryApply", method = RequestMethod.POST)
    @ApiOperation("盘点申请")
    @Permission
    public ResponseData InventoryApply(@RequestBody InventoryParam inventoryParam) {
        Inventory inventory = this.inventoryService.InventoryApply(inventoryParam);
        return ResponseData.success(inventory);
    }

    /**
     * 即时盘点添加
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/timelyAdd", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData timelyAddItem(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.timelyAdd(inventoryParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/selectCondition", method = RequestMethod.POST)
    @ApiOperation("条件盘点")
    public ResponseData selectCondition(@RequestBody InventoryParam inventoryParam) {
        this.inventoryService.selectCondition(inventoryParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/timely", method = RequestMethod.POST)
    @ApiOperation("及时盘点")
    public ResponseData timely(@RequestBody InventoryParam inventoryParam) {
        Object timely = this.inventoryService.timely(inventoryParam.getPositionId());
        return ResponseData.success(timely);
    }



    @RequestMapping(value = "{version}/timely", method = RequestMethod.POST)
    @ApiOperation("及时盘点")
    @ApiVersion("1.1")
    public ResponseData timelyV1(@RequestBody InventoryParam inventoryParam) {
        List<AnomalyResult> anomalyOrders = BeanUtil.copyToList(inventoryParam.getParams(), AnomalyResult.class);
        this.anomalyOrderService.inStock(anomalyOrders);
        return ResponseData.success();
    }


    @RequestMapping(value = "/conditionGetOne", method = RequestMethod.POST)
    public ResponseData conditionGetOne(@RequestBody InventoryDetailParam detailParam) {
        InventoryDetailResult result = this.inventoryService.conditionGetOne(detailParam);
        return ResponseData.success(result);
    }


    /**
     * 盘点超时
     *
     * @return
     */
    @RequestMapping(value = "/timeOut", method = RequestMethod.GET)
    public ResponseData timeOut() {

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
    public ResponseData detail(@RequestBody InventoryParam inventoryParam) {
        InventoryResult detail = this.inventoryService.detail(inventoryParam.getInventoryTaskId());
        return ResponseData.success(detail);
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

        if (LoginContextHolder.getContext().isAdmin()) {
            return this.inventoryService.findPageBySpec(inventoryParam,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  this.inventoryService.findPageBySpec(inventoryParam,dataScope);
        }
//        return this.inventoryService.findPageBySpec(inventoryParam);
    }


    @RequestMapping(value = "/listReceipt", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InventoryResult> listReceipt(@RequestBody(required = false) InventoryParam inventoryParam) {
        if (ToolUtil.isEmpty(inventoryParam)) {
            inventoryParam = new InventoryParam();
        }
        inventoryParam.setComplete(99);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.inventoryService.findPageBySpec(inventoryParam,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  this.inventoryService.findPageBySpec(inventoryParam,dataScope);
        }
    }


    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InventoryResult> pageList(@RequestBody(required = false) InventoryParam inventoryParam) {
        if (ToolUtil.isEmpty(inventoryParam)) {
            inventoryParam = new InventoryParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.inventoryService.pageList(inventoryParam,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  this.inventoryService.pageList(inventoryParam,dataScope);
        }
    }

}



package cn.atsoft.dasheng.supplier.controller;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.supplier.entity.SupplierBrand;
import cn.atsoft.dasheng.supplier.model.params.SupplierBrandParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBrandResult;
import cn.atsoft.dasheng.supplier.service.SupplierBrandService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
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
 * 供应商品牌绑定控制器
 *
 * @author song
 * @Date 2022-01-13 11:53:48
 */
@RestController
@RequestMapping("/supplierBrand")
@Api(tags = "供应商品牌绑定")
public class SupplierBrandController extends BaseController {

    @Autowired
    private SupplierBrandService supplierBrandService;
    @Autowired
    private SupplyService supplyService;


    /**
     * 当前物料下的所有供应商
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/getSupplierBySku", method = RequestMethod.GET)
    public ResponseData getSupplierBySku(@Param("skuId") Long skuId) {
        List<CustomerResult> supplierBySku = this.supplyService.getSupplierBySku(skuId);
        return ResponseData.success(supplierBySku);
    }

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SupplierBrandParam supplierBrandParam) {
        this.supplierBrandService.add(supplierBrandParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SupplierBrandParam supplierBrandParam) {

        this.supplierBrandService.update(supplierBrandParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SupplierBrandParam supplierBrandParam) {
        this.supplierBrandService.delete(supplierBrandParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SupplierBrandResult> detail(@RequestBody SupplierBrandParam supplierBrandParam) {
        SupplierBrand detail = this.supplierBrandService.getById(supplierBrandParam.getSupplierBrandId());
        SupplierBrandResult result = new SupplierBrandResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SupplierBrandResult> list(@RequestBody(required = false) SupplierBrandParam supplierBrandParam) {
        if (ToolUtil.isEmpty(supplierBrandParam)) {
            supplierBrandParam = new SupplierBrandParam();
        }
        return this.supplierBrandService.findPageBySpec(supplierBrandParam);
    }


}



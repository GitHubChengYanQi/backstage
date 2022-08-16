package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderDetailsResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
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
 * 产品订单详情控制器
 *
 * @author song
 * @Date 2021-10-20 16:18:02
 */
@RestController
@RequestMapping("/productOrderDetails")
@Api(tags = "产品订单详情")
public class ProductOrderDetailsController extends BaseController {

    @Autowired
    private ProductOrderDetailsService productOrderDetailsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductOrderDetailsParam productOrderDetailsParam) {
        this.productOrderDetailsService.add(productOrderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改产品订单详情", key = "name", dict = ProductOrderDetailsParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductOrderDetailsParam productOrderDetailsParam) {

        this.productOrderDetailsService.update(productOrderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除产品订单详情", key = "name", dict = ProductOrderDetailsParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductOrderDetailsParam productOrderDetailsParam) {
        this.productOrderDetailsService.delete(productOrderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductOrderDetailsParam productOrderDetailsParam) {
        ProductOrderDetails detail = this.productOrderDetailsService.getById(productOrderDetailsParam.getProductOrderDetailsId());
        ProductOrderDetailsResult result = new ProductOrderDetailsResult();
        ToolUtil.copyProperties(detail, result);

        ;
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductOrderDetailsResult> list(@RequestBody(required = false) ProductOrderDetailsParam productOrderDetailsParam) {
        if (ToolUtil.isEmpty(productOrderDetailsParam)) {
            productOrderDetailsParam = new ProductOrderDetailsParam();
        }
        return this.productOrderDetailsService.findPageBySpec(productOrderDetailsParam);
    }


}



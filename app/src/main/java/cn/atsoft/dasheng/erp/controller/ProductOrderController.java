package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
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
 * 产品订单控制器
 *
 * @author song
 * @Date 2021-10-20 16:18:02
 */
@RestController
@RequestMapping("/productOrder")
@Api(tags = "产品订单")
public class ProductOrderController extends BaseController {

    @Autowired
    private ProductOrderService productOrderService;
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
    public ResponseData addItem(@RequestBody ProductOrderParam productOrderParam) {
        this.productOrderService.add(productOrderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductOrderParam productOrderParam) {

        this.productOrderService.update(productOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductOrderParam productOrderParam) {
        this.productOrderService.delete(productOrderParam);
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
    public ResponseData<ProductOrderResult> detail(@RequestBody ProductOrderParam productOrderParam) {
        ProductOrder detail = this.productOrderService.getById(productOrderParam.getProductOrderId());
        List<ProductOrderDetails> productOrderDetails = productOrderDetailsService.lambdaQuery()
                .in(ProductOrderDetails::getProductOrderId, detail.getProductOrderId())
                .list();
        List<ProductOrderDetails> productOrderDetailsList = new ArrayList<>();
        for (ProductOrderDetails productOrderDetail : productOrderDetails) {
            productOrderDetailsList.add(productOrderDetail);
        }

        ProductOrderResult result = new ProductOrderResult();
        ToolUtil.copyProperties(detail, result);
        result.setProductOrderDetails(productOrderDetailsList);

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
    public PageInfo<ProductOrderResult> list(@RequestBody(required = false) ProductOrderParam productOrderParam) {
        if (ToolUtil.isEmpty(productOrderParam)) {
            productOrderParam = new ProductOrderParam();
        }
        return this.productOrderService.findPageBySpec(productOrderParam);
    }


}



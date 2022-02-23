package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import cn.atsoft.dasheng.purchase.pojo.QuotationParam;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 采购报价表单控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-22 11:43:30
 */
@RestController
@RequestMapping("/purchaseQuotation")
@Api(tags = "采购报价表单")
public class PurchaseQuotationController extends BaseController {

    @Autowired
    private PurchaseQuotationService purchaseQuotationService;



    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@Valid @RequestBody QuotationParam param) {

        this.purchaseQuotationService.addList(param);
        return ResponseData.success();
    }

//    /**
//     * 批量增加
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-22
//     */
//    @RequestMapping(value = "/addbatch", method = RequestMethod.POST)
//    @ApiOperation("批量增加")
//    public ResponseData addbatch(@RequestBody QuotationParam param) {
//        this.purchaseQuotationService.batchAdd(param);
//        return ResponseData.success();
//    }


    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PurchaseQuotationResult> detail(@RequestBody PurchaseQuotationParam purchaseQuotationParam) {
        PurchaseQuotation detail = this.purchaseQuotationService.getById(purchaseQuotationParam.getPurchaseQuotationId());
        PurchaseQuotationResult result = new PurchaseQuotationResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PurchaseQuotationResult> list(@RequestBody(required = false) PurchaseQuotationParam purchaseQuotationParam) {
        if (ToolUtil.isEmpty(purchaseQuotationParam)) {
            purchaseQuotationParam = new PurchaseQuotationParam();
        }
        return this.purchaseQuotationService.findPageBySpec(purchaseQuotationParam);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    @RequestMapping(value = "/allList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<PurchaseQuotationResult> allList(@RequestBody(required = false) PurchaseQuotationParam purchaseQuotationParam) {
        if (ToolUtil.isEmpty(purchaseQuotationParam)) {
            purchaseQuotationParam = new PurchaseQuotationParam();
        }
        return this.purchaseQuotationService.findListBySpec(purchaseQuotationParam);
    }

    /**
     * 通过sku查询
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    @RequestMapping(value = "/getListBySku", method = RequestMethod.GET)
    @ApiOperation("通过sku查询")
    public ResponseData getListBySku(@Param("skuId") Long skuId) {
        List<PurchaseQuotationResult> list = this.purchaseQuotationService.getListBySku(skuId);
        return ResponseData.success(list);
    }

}



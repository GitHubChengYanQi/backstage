package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuPrice;
import cn.atsoft.dasheng.erp.model.params.SkuPriceBatchParam;
import cn.atsoft.dasheng.erp.model.params.SkuPriceParam;
import cn.atsoft.dasheng.erp.model.result.SkuPriceResult;
import cn.atsoft.dasheng.erp.service.SkuPriceService;
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
 * 商品价格设置表控制器
 *
 * @author sjl
 * @Date 2023-01-09 15:14:58
 */
@RestController
@RequestMapping("/skuPrice")
@Api(tags = "商品价格设置表")
public class SkuPriceController extends BaseController {

    @Autowired
    private SkuPriceService skuPriceService;

    /**
     * 新增接口
     *
     * @author sjl
     * @Date 2023-01-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SkuPriceParam skuPriceParam) {
        this.skuPriceService.add(skuPriceParam);
        return ResponseData.success();
    }

    /**
     * 批量添加
     */
    @RequestMapping(value = "/addBatch", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addBatch(@RequestBody SkuPriceBatchParam skuPriceBatchParam) {
        for (SkuPriceParam skuPriceParam : skuPriceBatchParam.getSkuPriceParamList()) {
            this.skuPriceService.add(skuPriceParam);
        }
        return ResponseData.success();
    }









}



package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.AllBomService;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 仓库物品明细表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/stockDetails")
@Api(tags = "仓库物品明细表")
public class StockDetailsController extends BaseController {

    @Autowired
    private AllBom allBom;

    @Autowired
    private StockDetailsService stockDetailsService;


    @RequestMapping(value = "/getDetailsBySkuId", method = RequestMethod.POST)
    public ResponseData getDetailsBySkuId(@RequestBody StockDetailsParam stockDetailsParam) {
        List<StockDetailsResult> stockDetails = this.stockDetailsService.getDetailsBySkuId(stockDetailsParam.getSkuId());
        return ResponseData.success(stockDetails);
    }


    @RequestMapping(value = "/getAllSkuIds", method = RequestMethod.GET)
    public ResponseData getAllSkuIds(@RequestParam Long skuId, Integer num) {
        allBom.getSkuList().clear();
        allBom.getStockNumber().clear();
        allBom.getNotEnough().clear();
        allBom.getEnough().clear();
        allBom.getBom().clear();
        allBom.getBom(skuId, num);
        allBom.getNumber(skuId);
        return ResponseData.success(allBom);
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<StockDetailsResult> detail(@RequestBody StockDetailsParam stockDetailsParam) {
        StockDetails detail = this.stockDetailsService.getById(stockDetailsParam.getStockItemId());
        StockDetailsResult result = new StockDetailsResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StockDetailsResult> list(@RequestBody(required = false) StockDetailsParam stockDetailsParam) {
        if (ToolUtil.isEmpty(stockDetailsParam)) {
            stockDetailsParam = new StockDetailsParam();
        }
//        return this.stockDetailsService.findPageBySpec(stockDetailsParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.stockDetailsService.findPageBySpec(stockDetailsParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.stockDetailsService.findPageBySpec(stockDetailsParam, dataScope);
        }
    }

    /**
     * 返回sku
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/backSkuByStoreHouse", method = RequestMethod.GET)
    @ApiOperation("编辑")
    public ResponseData backSkuByStoreHouse(@RequestParam Long id) {

        List<Long> longs = this.stockDetailsService.backSkuByStoreHouse(id);
        return ResponseData.success(longs);
    }


}



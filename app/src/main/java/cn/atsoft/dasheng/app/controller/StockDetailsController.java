package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.request.StoreHouseAndSkuNumber;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.app.pojo.AllBomParam;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
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
import cn.atsoft.dasheng.production.model.request.StockSkuTotal;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;


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
    private StockDetailsService stockDetailsService;
    @Autowired
    private StorehouseService storehouseService;


    @RequestMapping(value = "/getDetailsBySkuId", method = RequestMethod.POST)
    public ResponseData getDetailsBySkuId(@RequestBody StockDetailsParam stockDetailsParam) {
        List<StockDetailsResult> stockDetails = this.stockDetailsService.getDetailsBySkuId(stockDetailsParam.getSkuId());
        return ResponseData.success(stockDetails);
    }


    @RequestMapping(value = "/stockSkuBrands", method = RequestMethod.GET)
    public ResponseData stockSkuBrands() {
        List<StockSkuBrand> stockSkuBrands = this.stockDetailsService.stockSkuBrands();
        return ResponseData.success(stockSkuBrands);
    }


    @RequestMapping(value = "/getNumberByStock", method = RequestMethod.POST)
    public ResponseData getNumberByStock(@RequestBody StockDetailsParam stockDetailsParam) {
        Integer number = this.stockDetailsService.getNumberByStock(stockDetailsParam.getSkuId(), stockDetailsParam.getBrandId(), stockDetailsParam.getStorehousePositionsId());
        return ResponseData.success(number);
    }

    @RequestMapping(value = "/splitInKind", method = RequestMethod.POST)
    public ResponseData splitInKind(@RequestBody StockDetailsParam stockDetailsParam) {
        this.stockDetailsService.splitInKind(stockDetailsParam.getInkindId());
        return ResponseData.success();
    }

    /**
     * 库存报表
     *
     * @return
     */
    @RequestMapping(value = "/startAnalyse", method = RequestMethod.GET)
    public ResponseData startAnalyse() {
        this.stockDetailsService.statement();
        return ResponseData.success();
    }

    /**
     * 库存物料详细信息
     *
     * @param stockDetailsParam
     * @return
     */
    @RequestMapping(value = "/inkindList", method = RequestMethod.POST)
    public ResponseData inkindList(@RequestBody StockDetailsParam stockDetailsParam) {
        this.stockDetailsService.inkindList(stockDetailsParam.getSkuId());
        return ResponseData.success();
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
//        if (LoginContextHolder.getContext().isAdmin()) {
        return this.stockDetailsService.findPageBySpec(stockDetailsParam, null);
//        } else {
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
//            return this.stockDetailsService.findPageBySpec(stockDetailsParam, dataScope);
//        }
    }


    @RequestMapping(value = "/getInkind", method = RequestMethod.POST)
    public ResponseData getInkind(@RequestBody StockDetailsParam stockDetailsParam) {
        StockDetails detail = this.stockDetailsService.getInkind(stockDetailsParam);
        return ResponseData.success(detail);
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

    /**
     * 根据skuId返回仓库
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/getStockNumberBySkuId", method = RequestMethod.GET)
    @ApiOperation("编辑")
    public ResponseData getStockNumberBySkuId(@RequestParam Long skuId, @RequestParam Long storehouseId) {

        List<StockDetailsResult> stockNumberBySkuId = this.stockDetailsService.getStockNumberBySkuId(skuId, storehouseId);
        return ResponseData.success(stockNumberBySkuId);
    }

    /**
     * 返回sku
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/getStockAndNumberBySkuId", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData getStockAndNumberBySkuId(@RequestBody StockDetailsParam stockDetailsParam) {
        List<StockDetails> list = this.stockDetailsService.query().eq("display", 1).eq("stage", 1).eq("sku_id", stockDetailsParam.getSkuId()).ne("storehouse_id", stockDetailsParam.getStorehouseId()).list();

        List<StockDetails> totalList = new ArrayList<>();
        list.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getStorehouseId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<StoreHouseAndSkuNumber> storeHouseAndSkuNumbers = BeanUtil.copyToList(totalList, StoreHouseAndSkuNumber.class);
        List<Long> storehouseIds = new ArrayList<>();
        for (StoreHouseAndSkuNumber storeHouseAndSkuNumber : storeHouseAndSkuNumbers) {
            storehouseIds.add(storeHouseAndSkuNumber.getStorehouseId());
        }

        List<StorehouseResult> storehouseResults = storehouseIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(storehouseIds), StorehouseResult.class);
        for (StoreHouseAndSkuNumber storeHouseAndSkuNumber : storeHouseAndSkuNumbers) {
            for (StorehouseResult storehouseResult : storehouseResults) {
                if (storeHouseAndSkuNumber.getStorehouseId().equals(storehouseResult.getStorehouseId())) {
                    storeHouseAndSkuNumber.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
        return ResponseData.success(storeHouseAndSkuNumbers);
    }


}



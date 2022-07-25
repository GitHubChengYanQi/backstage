package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.entity.ShopCart;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceResult;
import cn.atsoft.dasheng.erp.model.result.MaintenanceSelectSku;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.MaintenanceService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ShopCartService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 养护申请主表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-06-28 09:41:30
 */
@RestController
@RequestMapping("/maintenance")
@Api(tags = "养护申请主表")
public class MaintenanceController extends BaseController {

    @Autowired
    private MaintenanceService maintenanceService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceParam maintenanceParam) {
        Maintenance maintenance = this.maintenanceService.add(maintenanceParam);
        return ResponseData.success(maintenance);
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MaintenanceParam maintenanceParam) {

        this.maintenanceService.update(maintenanceParam);
        return ResponseData.success();
    }

//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-06-28
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody MaintenanceParam maintenanceParam)  {
//        this.maintenanceService.delete(maintenanceParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MaintenanceResult> detail(@RequestBody MaintenanceParam maintenanceParam) {
        Maintenance detail = this.maintenanceService.getById(maintenanceParam.getMaintenanceId());
        MaintenanceResult result = new MaintenanceResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }
    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/viewDetail", method = RequestMethod.POST)
    @ApiOperation("查看筛选物料")
    public ResponseData viewDetail(@RequestBody MaintenanceParam maintenanceParam) {
        if(ToolUtil.isEmpty(maintenanceParam.getSelectParams())){
            return ResponseData.success();
        }
        String jsonString = JSON.toJSONString(maintenanceParam.getSelectParams());
        Maintenance maintenance = new Maintenance();
        maintenance.setSelectParams(jsonString);
        if (ToolUtil.isNotEmpty(maintenanceParam.getNearMaintenance())) {
            maintenance.setNearMaintenance(maintenanceParam.getNearMaintenance());
        }


        List<StockDetails> stockDetails = this.maintenanceService.needMaintenanceByRequirement(maintenance);
        List<StockDetails> totalList = new ArrayList<>();
        stockDetails.parallelStream().collect(Collectors.groupingBy(StockDetails::getSkuId, Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                    }}).ifPresent(totalList::add);
                }
        );
        List<StockDetailsResult> stockDetailsResults = BeanUtil.copyToList(totalList, StockDetailsResult.class);
        List<Long> skuIds = new ArrayList<>();

        for (StockDetailsResult stockDetailsResult : stockDetailsResults) {
            skuIds.add(stockDetailsResult.getSkuId());
        }

        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);

        for (StockDetailsResult stockDetailsResult : stockDetailsResults) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (stockDetailsResult.getSkuId().equals(skuSimpleResult.getSkuId())){
                    stockDetailsResult.setSkuResult(skuSimpleResult);
                    break;
                }
            }
        }

        return ResponseData.success(stockDetailsResults);
    }
    @RequestMapping(value = "/getDetails", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData getDetails(@RequestBody(required = false) MaintenanceDetailParam maintenanceDetailParam) {

        return ResponseData.success(this.maintenanceService.getDetails(maintenanceDetailParam.getMaintenanceId()));
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceResult> list(@RequestBody(required = false) MaintenanceParam maintenanceParam) {
        if(ToolUtil.isEmpty(maintenanceParam)){
            maintenanceParam = new MaintenanceParam();
        }
        return this.maintenanceService.findPageBySpec(maintenanceParam);
    }
/**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/findTaskByTime", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData findTaskByTime() {
        List<Maintenance> taskByTime = this.maintenanceService.findTaskByTime();
        List<MaintenanceResult> maintenanceResults = BeanUtil.copyToList(taskByTime, MaintenanceResult.class);
        maintenanceService.format(maintenanceResults);
        return ResponseData.success(maintenanceResults) ;
    }
    /* 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/findSkuInStoreHouse", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData findSkuInStoreHouse(@RequestBody(required = false) MaintenanceParam maintenanceParam) {
        List<ShopCart> shopCarts = shopCartService.query().eq("type", "allocation").eq("create_user", LoginContextHolder.getContext().getUserId()).eq("display", 1).list();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandId = new ArrayList<>();
        for (ShopCart shopCart : shopCarts) {
            skuIds.add(shopCart.getSkuId());
            brandId.add(shopCart.getBrandId());
        }
        List<StockDetails> list = stockDetailsService.query().in("sku_id", skuIds).in("brand_id", brandId).eq("display", 1).eq("stage", 1).list();
        List<StockDetails> totalList = new ArrayList<>();
        list.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() +"_"+item.getBrandId()+"_"+item.getStorehouseId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<SkuSimpleResult> skuSimpleResultList = skuService.simpleFormatSkuResult(skuIds);
        List<MaintenanceSelectSku> results = new ArrayList<>();
        for (ShopCart shopCart : shopCarts) {
            MaintenanceSelectSku result = new MaintenanceSelectSku();
            for (SkuSimpleResult skuSimpleResult : skuSimpleResultList) {
                if (shopCart.getSkuId().equals(skuSimpleResult.getSkuId())){
                    result.setSkuResult(skuSimpleResult);
                }
            }
            List<StockDetails> bySku = new ArrayList<>();
            for (StockDetails stockDetails : totalList) {
                if (stockDetails.getBrandId().equals(shopCart.getBrandId()) && stockDetails.getSkuId().equals(shopCart.getSkuId())){
                    bySku.add(stockDetails);
                }
            }
            result.setStockDetails(bySku);
            results.add(result);
        }






        return ResponseData.success(results) ;
    }




}



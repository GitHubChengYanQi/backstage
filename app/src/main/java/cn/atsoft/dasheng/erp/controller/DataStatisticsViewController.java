package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.request.InstockView;
import cn.atsoft.dasheng.app.model.request.OutStockDetailView;
import cn.atsoft.dasheng.app.model.request.OutStockView;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statisticalView")
@Api(tags = "统计视图")
public class DataStatisticsViewController extends BaseController {

    @Autowired
    private InstockReceiptService instockReceiptService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private OutstockOrderService outstockOrderService;

    @Autowired
    private MaintenanceLogService maintenanceLogService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private AllocationLogService allocationLogService;

    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private AnomalyService anomalyService;

    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductionPickListsCartService pickListsCartService;
    @Autowired
    private ProductionPickListsDetailService pickListsDetailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;


    @RequestMapping(value = "/taskCountView", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData taskCountView() {
        Integer doneCount = activitiProcessTaskService.query().eq("status", 99).count();
        Integer startingCount = activitiProcessTaskService.query().eq("status", 0).count();
        List<ActivitiProcessTask> maintenanceTasks = activitiProcessTaskService.query().eq("status", 0).eq("type", "MAINTENANCE").list();
        List<ActivitiProcessTask> stocktakingTasks = activitiProcessTaskService.query().eq("status", 0).eq("type", "Stocktaking").list();
        List<Long> stocktakingIds = new ArrayList<>();
        for (ActivitiProcessTask stocktakingTask : stocktakingTasks) {
            stocktakingIds.add(stocktakingTask.getFormId());
        }
        List<Inventory> inventories = stocktakingIds.size() == 0 ? new ArrayList<>() : inventoryService.listByIds(stocktakingIds);
        int overdueCount = 0;
        for (Inventory inventory : inventories) {
            if (inventory.getEndTime().getTime() < DateUtil.date().getTime()) {
                overdueCount += 1;
            }
        }
        List<Long> maintenanceIds = new ArrayList<>();
        for (ActivitiProcessTask maintenanceTask : maintenanceTasks) {
            maintenanceIds.add(maintenanceTask.getFormId());
        }
        List<Maintenance> maintenances = maintenanceIds.size() == 0 ? new ArrayList<>() : new ArrayList<>();


        for (Maintenance maintenance : maintenances) {
            if (maintenance.getEndTime().getTime() < DateUtil.date().getTime()) {
                overdueCount += 1;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("doneCount", doneCount);
        result.put("startingCount", startingCount);
        result.put("overdueCount", overdueCount);

        return ResponseData.success(result);
    }


    @RequestMapping(value = "/billCountView", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData billCountView() {
        Integer instockCount = instockReceiptService.query().eq("display", 1).count();
        Integer outstockCont = outstockOrderService.query().eq("display", 1).count();
        Integer maintenanceCount = maintenanceLogService.query().eq("display", 1).count();
        Integer inventoryCount = inventoryService.query().eq("display", 1).count();
        Integer allocationCount = allocationLogService.query().eq("display", 1).count();
        Map<String, Object> result = new HashMap<>();
        result.put("instockCount", instockCount);
        result.put("outstockCont", outstockCont);
        result.put("maintenanceCount", maintenanceCount);
        result.put("inventoryCount", inventoryCount);
        result.put("allocationCount", allocationCount);
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/instockView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo instockView(@RequestBody DataStatisticsViewParam param) {
        return PageFactory.createPageInfo(instockOrderService.instockView(param));
    }

    @RequestMapping(value = "/instockDetailView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockDetailView(@RequestBody DataStatisticsViewParam param) {
        return ResponseData.success(instockOrderService.viewDetail(param));
    }

    @RequestMapping(value = "/viewTotail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData viewTotail(@RequestBody DataStatisticsViewParam param) {
        return ResponseData.success(instockOrderService.viewTotail(param));
    }
    @RequestMapping(value = "/outstockViewTotail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outstockViewTotail(@RequestBody DataStatisticsViewParam param) {
        OutStockView outStockView = new OutStockView();
        int skuCount = 0;
        int numCount = 0;
        List<Long> skuIds = new ArrayList<>();
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsDetail> list1 =pickLists.size() == 0 ? new ArrayList<>() : pickListsDetailService.lambdaQuery().in(ProductionPickListsDetail::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).list();
        for (ProductionPickListsDetail listsDetail : list1) {
            numCount+=listsDetail.getReceivedNumber();
            skuIds.add(listsDetail.getSkuId());
        }
        outStockView.setOutSkuCount(skuIds.stream().distinct().collect(Collectors.toList()).size());
        outStockView.setOutNumCount(numCount);
        outStockView.setOrderCount(pickLists.stream().distinct().collect(Collectors.toList()).size());
        return ResponseData.success(outStockView);
    }


    @RequestMapping(value = "/outstockView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outStockView(@RequestBody DataStatisticsViewParam param) {
        Page<OutStockView> outStockViewPage = pickListsService.outStockUserView(param);
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(outStockViewPage.getRecords().stream().map(OutStockView::getUserId).collect(Collectors.toList()));
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        for (OutStockView record : outStockViewPage.getRecords()) {
            for (UserResult userResultsById : userResultsByIds) {
                if  (record.getUserId().equals(userResultsById.getUserId())){
                    record.setUserResult(userResultsById);
                    break;
                }
            }
            int outNumCount = 0;
            int pickNumCount = 0;
            List<Long> outSkuList = new ArrayList<>();
            List<Long> pickSkuList = new ArrayList<>();
            for (ProductionPickLists pickList : pickLists) {
                if (record.getUserId().equals(pickList.getUserId())) {
                    for (ProductionPickListsCart cart : carts) {
                        if (pickList.getPickListsId().equals(cart.getPickListsId())) {
                            outNumCount += cart.getNumber();
                            outSkuList.add(cart.getSkuId());
                        }
                    }
                }
            }
            for (ProductionPickListsCart cart : carts) {
                if (cart.getCreateUser().equals(record.getUserId())) {
                    pickNumCount += cart.getNumber();
                    pickSkuList.add(cart.getSkuId());
                }
            }
            int outSkuCount = outSkuList.stream().distinct().collect(Collectors.toList()).size();
            int pickSkuCount = pickSkuList.stream().distinct().collect(Collectors.toList()).size();
            record.setPickNumCount(pickNumCount);
            record.setPickSkuCount(pickSkuCount);
            record.setOutNumCount(outNumCount);
            record.setOutSkuCount(outSkuCount);
        }
        return PageFactory.createPageInfo(outStockViewPage);
    }

    @RequestMapping(value = "/outStockDetailView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockDetailView(@RequestBody DataStatisticsViewParam param) {
//        pickListsDetailService.lambdaQuery()
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();
        List<OutStockDetailView> results = new ArrayList<>();
        List<OutStockDetailView> results2 = new ArrayList<>();


        for (ProductionPickLists pickList : pickLists) {

            for (ProductionPickListsCart cart : carts) {
                if (cart.getPickListsId().equals(pickList.getPickListsId()) && cart.getStatus().equals(99)){
                    OutStockDetailView result = new OutStockDetailView();
                    result.setSkuId(cart.getSkuId());
                    if(param.getUserId().equals(pickList.getUserId())){
                        result.setOutNumCount(cart.getNumber());
                    }else {
                        result.setOutNumCount(0);
                    }
                    result.setBrandId(cart.getBrandId());
                    results.add(result);
                }
            }
        }
        for (ProductionPickLists pickList : pickLists) {

            for (ProductionPickListsCart cart : carts) {
                if (cart.getPickListsId().equals(pickList.getPickListsId())&& cart.getCreateUser().equals(param.getUserId())) {
                    OutStockDetailView result = new OutStockDetailView();
                    result.setSkuId(cart.getSkuId());
                    if (cart.getCreateUser().equals(param.getUserId())){
                        result.setPickNumCount(cart.getNumber());
                    }else {
                        result.setPickNumCount(0);
                    }
                    result.setBrandId(cart.getBrandId());
                    results2.add(result);
                }
            }
        }



        List<OutStockDetailView> totalResults = new ArrayList<>();

        results.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) , Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
                        setOutNumCount(a.getOutNumCount()+b.getOutNumCount());
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(totalResults::add);
                }
        );
        List<OutStockDetailView> totalResults2 = new ArrayList<>();

        results2.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) , Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
                        setPickNumCount(a.getPickNumCount()+b.getPickNumCount());
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(totalResults2::add);
                }
        );

        for (OutStockDetailView totalResult : totalResults) {
            for (OutStockDetailView outStockDetailView : totalResults2) {
                if (totalResult.getSkuId().equals(outStockDetailView.getSkuId()) && totalResult.getBrandId().equals(outStockDetailView.getBrandId())){
                    totalResult.setPickNumCount(outStockDetailView.getPickNumCount());
                    break;
                }
            }
        }
        List<SkuSimpleResult> skuSimpleResults = totalResults.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
        List<BrandResult> brandResults = totalResults.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
        for (OutStockDetailView totalResult : totalResults) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (totalResult.getSkuId().equals(skuSimpleResult.getSkuId())){
                    totalResult.setSkuResult(skuSimpleResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (totalResult.getBrandId().equals(brandResult.getBrandId())){
                    totalResult.setBrandResult(brandResult);
                    break;
                }
            }
        }


        return ResponseData.success(totalResults);
    }

}

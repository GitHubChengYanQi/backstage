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
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
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
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    public PageInfo outstockViewTotail(@RequestBody DataStatisticsViewParam param) {
//        OutStockView outStockView = new OutStockView();
//        int skuCount = 0;
//        int numCount = 0;
//        List<Long> skuIds = new ArrayList<>();
//        List<OutStockView> pickListsView =pickListsService.outStockView(param);
//        for (ProductionPickListsDetail listsDetail : list1) {
//            numCount+=listsDetail.getReceivedNumber();
//            skuIds.add(listsDetail.getSkuId());
//        }
//        outStockView.setOutSkuCount(skuIds.stream().distinct().collect(Collectors.toList()).size());
//        outStockView.setOutNumCount(numCount);
//        outStockView.setOrderCount(pickLists.stream().distinct().collect(Collectors.toList()).size());


        switch (param.getViewMode()) {
            case "pickUser":
                Page<UserResult> userResultPage = userService.userResultPageList(new UserParam());
                Page<OutStockView> outStockViewPage = new Page<>();
                ToolUtil.copyProperties(userResultPage, outStockViewPage);

                List<UserResult> userResults = userResultPage.getRecords();
                //查询领料人 关联 出库单
                LambdaQueryChainWrapper<ProductionPickLists> pickListsWrapper = pickListsService.lambdaQuery().in(ProductionPickLists::getUserId, userResults.stream().map(UserResult::getUserId).collect(Collectors.toList()));
                List<ProductionPickLists> pickLists = userResults.size() == 0 ? new ArrayList<>() : pickListsWrapper.list();
                LambdaQueryChainWrapper<ProductionPickListsDetail> detailWrapper = pickListsDetailService.lambdaQuery().in(ProductionPickListsDetail::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList()));
                //出库单详情
                List<ProductionPickListsDetail> pickListsDetails = pickLists.size() == 0 ? new ArrayList<>() : detailWrapper.list();
                List<SkuSimpleResult> skuResult = pickListsDetails.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(pickListsDetails.stream().map(ProductionPickListsDetail::getSkuId).distinct().collect(Collectors.toList()));
                List<OutStockView> results = new ArrayList<>();
                for (UserResult userResult : userResults) {
                    OutStockView result = new OutStockView();
                    List<OutStockView.SkuAndNumber> skuAndNumbers = new ArrayList<>();//领料人领到的物料与数量
                    result.setUserResult(userResult);
                    for (ProductionPickLists pickList : pickLists) {
                        if (pickList.getUserId().equals(userResult.getUserId())) {
                            for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                if (pickListsDetail.getPickListsId().equals(pickList.getPickListsId())) {
                                    OutStockView.SkuAndNumber skuAndNumber = new OutStockView.SkuAndNumber();
                                    skuAndNumber.setSkuId(pickListsDetail.getSkuId());
                                    skuAndNumber.setNumber(pickListsDetail.getReceivedNumber());
                                    skuAndNumbers.add(skuAndNumber);
                                }
                            }
                        }
                    }
                    /**
                     * 数据组合
                     */
                    List<OutStockView.SkuAndNumber> totalList = new ArrayList<>();
                    skuAndNumbers.parallelStream().collect(Collectors.groupingBy(OutStockView.SkuAndNumber::getSkuId, Collectors.toList())).forEach(
                            (id, transfer) -> {
                                transfer.stream().reduce((a, b) -> new OutStockView.SkuAndNumber() {{
                                    setNumber(a.getNumber() + b.getNumber());
                                    setSkuId(a.getSkuId());

                                }}).ifPresent(totalList::add);
                            }
                    );
                    for (OutStockView.SkuAndNumber skuAndNumber : totalList) {
                        for (SkuSimpleResult skuSimpleResult : skuResult) {
                            if (skuAndNumber.getSkuId().equals(skuSimpleResult.getSkuId())) {
                                skuAndNumber.setSkuResult(skuSimpleResult);
                            }
                        }
                    }
                    result.setSkuAndNumbers(totalList);
                    results.add(result);
                }
                outStockViewPage.setRecords(results);
                return PageFactory.createPageInfo(outStockViewPage);

            case "sku":

                Page<SkuResult> skuPage = skuService.skuPage(new SkuParam());
                List<SkuResult> skuResultList = skuPage.getRecords();
                List<ProductionPickListsDetail> details = skuResultList.size() == 0 ? new ArrayList<>() : pickListsDetailService.lambdaQuery().in(ProductionPickListsDetail::getSkuId, skuResultList.stream().map(SkuResult::getSkuId).collect(Collectors.toList())).list();
                List<ProductionPickLists> productionPickLists = details.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(details.stream().map(ProductionPickListsDetail::getPickListsId).collect(Collectors.toList()));
                List<UserResult> userResultsByIds = productionPickLists.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(productionPickLists.stream().map(ProductionPickLists::getUserId).collect(Collectors.toList()));
                List<OutStockView> results1 = new ArrayList<>();
                for (SkuResult sku : skuResultList) {
                    OutStockView result = new OutStockView();
                    result.setSkuId(sku.getSkuId());
                    result.setSkuResult(BeanUtil.copyProperties(sku, SkuSimpleResult.class));
                    List<OutStockView.UserAndNumber> userAndNumbers = new ArrayList<>();
                    for (ProductionPickListsDetail detail : details) {
                        if (sku.getSkuId().equals(detail.getSkuId())) {
                            for (ProductionPickLists productionPickList : productionPickLists) {
                                if (detail.getPickListsId().equals(productionPickList.getPickListsId())) {
                                    OutStockView.UserAndNumber userAndNumber = new OutStockView.UserAndNumber();
                                    userAndNumber.setNumber(detail.getReceivedNumber());
                                    userAndNumber.setUserId(productionPickList.getUserId());
                                    userAndNumbers.add(userAndNumber);
                                }
                            }

                        }
                    }


                    List<OutStockView.UserAndNumber> totalList = new ArrayList<>();
                    userAndNumbers.parallelStream().collect(Collectors.groupingBy(OutStockView.UserAndNumber::getUserId, Collectors.toList())).forEach(
                            (id, transfer) -> {
                                transfer.stream().reduce((a, b) -> new OutStockView.UserAndNumber() {{
                                    setNumber(a.getNumber() + b.getNumber());
                                    setUserId(a.getUserId());

                                }}).ifPresent(totalList::add);
                            }
                    );
                    for (UserResult userResult : userResultsByIds) {
                        for (OutStockView.UserAndNumber userAndNumber : totalList) {
                            if (userAndNumber.getUserId().equals(userResult.getUserId())) {
                                userAndNumber.setUserResult(userResult);
                            }
                        }
                    }
                    result.setUserAndNumbers(totalList);
                    results1.add(result);
                }
                Page<OutStockView> SkuResult = new Page<>();
                ToolUtil.copyProperties(skuPage,SkuResult);
                SkuResult.setRecords(results1);
                return PageFactory.createPageInfo(SkuResult);

            case "log":
                break;
        }


//        return ResponseData.success(outStockView);
        return null;
    }


    @RequestMapping(value = "/outstockView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outStockView(@RequestBody DataStatisticsViewParam param) {
        Page<OutStockView> outStockViewPage = pickListsService.outStockUserView(param);
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(outStockViewPage.getRecords().stream().map(OutStockView::getUserId).collect(Collectors.toList()));
        List<OutStockView> pickListsView = pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists = pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        for (OutStockView record : outStockViewPage.getRecords()) {
            for (UserResult userResultsById : userResultsByIds) {
                if (record.getUserId().equals(userResultsById.getUserId())) {
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
        List<OutStockView> pickListsView = pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists = pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();
        List<OutStockDetailView> results = new ArrayList<>();
        List<OutStockDetailView> results2 = new ArrayList<>();


        for (ProductionPickLists pickList : pickLists) {

            for (ProductionPickListsCart cart : carts) {
                if (cart.getPickListsId().equals(pickList.getPickListsId()) && cart.getStatus().equals(99)) {
                    OutStockDetailView result = new OutStockDetailView();
                    result.setSkuId(cart.getSkuId());
                    if (param.getUserId().equals(pickList.getUserId())) {
                        result.setOutNumCount(cart.getNumber());
                    } else {
                        result.setOutNumCount(0);
                    }
                    result.setBrandId(cart.getBrandId());
                    results.add(result);
                }
            }
        }
        for (ProductionPickLists pickList : pickLists) {

            for (ProductionPickListsCart cart : carts) {
                if (cart.getPickListsId().equals(pickList.getPickListsId()) && cart.getCreateUser().equals(param.getUserId())) {
                    OutStockDetailView result = new OutStockDetailView();
                    result.setSkuId(cart.getSkuId());
                    if (cart.getCreateUser().equals(param.getUserId())) {
                        result.setPickNumCount(cart.getNumber());
                    } else {
                        result.setPickNumCount(0);
                    }
                    result.setBrandId(cart.getBrandId());
                    results2.add(result);
                }
            }
        }


        List<OutStockDetailView> totalResults = new ArrayList<>();

        results.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
                        setOutNumCount(a.getOutNumCount() + b.getOutNumCount());
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(totalResults::add);
                }
        );
        List<OutStockDetailView> totalResults2 = new ArrayList<>();

        results2.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
                        setPickNumCount(a.getPickNumCount() + b.getPickNumCount());
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(totalResults2::add);
                }
        );

        for (OutStockDetailView totalResult : totalResults) {
            for (OutStockDetailView outStockDetailView : totalResults2) {
                if (totalResult.getSkuId().equals(outStockDetailView.getSkuId()) && totalResult.getBrandId().equals(outStockDetailView.getBrandId())) {
                    totalResult.setPickNumCount(outStockDetailView.getPickNumCount());
                    break;
                }
            }
        }
        List<SkuSimpleResult> skuSimpleResults = totalResults.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
        List<BrandResult> brandResults = totalResults.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
        for (OutStockDetailView totalResult : totalResults) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (totalResult.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    totalResult.setSkuResult(skuSimpleResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (totalResult.getBrandId().equals(brandResult.getBrandId())) {
                    totalResult.setBrandResult(brandResult);
                    break;
                }
            }
        }


        return ResponseData.success(totalResults);
    }

}

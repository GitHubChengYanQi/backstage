package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.*;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    private OutstockOrderMapper outstockOrderMapper;
    @Autowired
    private OutstockListingMapper outstockListingMapper;

    @Autowired
    private ProductionPickListsMapper pickListsMapper;
    @Autowired
    private InstockOrderMapper instockOrderMapper;
    @Autowired
    private InstockListMapper instockListMapper;
    @Autowired
    private AnomalyMapper anomalyMapper;
    @Autowired
    private InstockLogDetailMapper instockLogDetailMapper;


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
                Page<StockView> outStockViewPage = new Page<>();
                ToolUtil.copyProperties(userResultPage, outStockViewPage);

                List<UserResult> userResults = userResultPage.getRecords();
                //查询领料人 关联 出库单
                LambdaQueryChainWrapper<ProductionPickLists> pickListsWrapper = pickListsService.lambdaQuery().in(ProductionPickLists::getUserId, userResults.stream().map(UserResult::getUserId).collect(Collectors.toList()));
                List<ProductionPickLists> pickLists = userResults.size() == 0 ? new ArrayList<>() : pickListsWrapper.list();
                LambdaQueryChainWrapper<ProductionPickListsDetail> detailWrapper = pickListsDetailService.lambdaQuery().in(ProductionPickListsDetail::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList()));
                //出库单详情
                List<ProductionPickListsDetail> pickListsDetails = pickLists.size() == 0 ? new ArrayList<>() : detailWrapper.list();
                List<SkuSimpleResult> skuResult = pickListsDetails.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(pickListsDetails.stream().map(ProductionPickListsDetail::getSkuId).distinct().collect(Collectors.toList()));
                List<StockView> results = new ArrayList<>();
                for (UserResult userResult : userResults) {
                    StockView result = new StockView();
                    List<StockView.SkuAndNumber> skuAndNumbers = new ArrayList<>();//领料人领到的物料与数量
                    result.setUserResult(userResult);
                    for (ProductionPickLists pickList : pickLists) {
                        if (pickList.getUserId().equals(userResult.getUserId())) {
                            for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                if (pickListsDetail.getPickListsId().equals(pickList.getPickListsId())) {
                                    StockView.SkuAndNumber skuAndNumber = new StockView.SkuAndNumber();
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
                    List<StockView.SkuAndNumber> totalList = new ArrayList<>();
                    skuAndNumbers.parallelStream().collect(Collectors.groupingBy(StockView.SkuAndNumber::getSkuId, Collectors.toList())).forEach(
                            (id, transfer) -> {
                                transfer.stream().reduce((a, b) -> new StockView.SkuAndNumber() {{
                                    setNumber(a.getNumber() + b.getNumber());
                                    setSkuId(a.getSkuId());

                                }}).ifPresent(totalList::add);
                            }
                    );
                    for (StockView.SkuAndNumber skuAndNumber : totalList) {
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
                List<StockView> results1 = new ArrayList<>();
                for (SkuResult sku : skuResultList) {
                    StockView result = new StockView();
                    result.setSkuId(sku.getSkuId());
                    result.setSkuResult(BeanUtil.copyProperties(sku, SkuSimpleResult.class));
                    List<StockView.UserAndNumber> userAndNumbers = new ArrayList<>();
                    for (ProductionPickListsDetail detail : details) {
                        if (sku.getSkuId().equals(detail.getSkuId())) {
                            for (ProductionPickLists productionPickList : productionPickLists) {
                                if (detail.getPickListsId().equals(productionPickList.getPickListsId())) {
                                    StockView.UserAndNumber userAndNumber = new StockView.UserAndNumber();
                                    userAndNumber.setNumber(detail.getReceivedNumber());
                                    userAndNumber.setUserId(productionPickList.getUserId());
                                    userAndNumbers.add(userAndNumber);
                                }
                            }

                        }
                    }


                    List<StockView.UserAndNumber> totalList = new ArrayList<>();
                    userAndNumbers.parallelStream().collect(Collectors.groupingBy(StockView.UserAndNumber::getUserId, Collectors.toList())).forEach(
                            (id, transfer) -> {
                                transfer.stream().reduce((a, b) -> new StockView.UserAndNumber() {{
                                    setNumber(a.getNumber() + b.getNumber());
                                    setUserId(a.getUserId());

                                }}).ifPresent(totalList::add);
                            }
                    );
                    for (UserResult userResult : userResultsByIds) {
                        for (StockView.UserAndNumber userAndNumber : totalList) {
                            if (userAndNumber.getUserId().equals(userResult.getUserId())) {
                                userAndNumber.setUserResult(userResult);
                            }
                        }
                    }
                    result.setUserAndNumbers(totalList);
                    results1.add(result);
                }
                Page<StockView> SkuResult = new Page<>();
                ToolUtil.copyProperties(skuPage, SkuResult);
                SkuResult.setRecords(results1);
                return PageFactory.createPageInfo(SkuResult);

            case "log":
                break;
        }


//        return ResponseData.success(outStockView);
        return null;
    }


//    @RequestMapping(value = "/outstockView", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public PageInfo outStockView(@RequestBody DataStatisticsViewParam param) {
//        Page<OutStockView> outStockViewPage = pickListsService.outStockUserView(param);
//        List<UserResult> userResultsByIds = userService.getUserResultsByIds(outStockViewPage.getRecords().stream().map(OutStockView::getUserId).collect(Collectors.toList()));
//        List<OutStockView> pickListsView = pickListsService.outStockView(param);
//        List<ProductionPickLists> pickLists = pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
//        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();
//
//        for (OutStockView record : outStockViewPage.getRecords()) {
//            for (UserResult userResultsById : userResultsByIds) {
//                if (record.getUserId().equals(userResultsById.getUserId())) {
//                    record.setUserResult(userResultsById);
//                    break;
//                }
//            }
//            int outNumCount = 0;
//            int pickNumCount = 0;
//            List<Long> outSkuList = new ArrayList<>();
//            List<Long> pickSkuList = new ArrayList<>();
//            for (ProductionPickLists pickList : pickLists) {
//                if (record.getUserId().equals(pickList.getUserId())) {
//                    for (ProductionPickListsCart cart : carts) {
//                        if (pickList.getPickListsId().equals(cart.getPickListsId())) {
//                            outNumCount += cart.getNumber();
//                            outSkuList.add(cart.getSkuId());
//                        }
//                    }
//                }
//            }
//            for (ProductionPickListsCart cart : carts) {
//                if (cart.getCreateUser().equals(record.getUserId())) {
//                    pickNumCount += cart.getNumber();
//                    pickSkuList.add(cart.getSkuId());
//                }
//            }
//            int outSkuCount = outSkuList.stream().distinct().collect(Collectors.toList()).size();
//            int pickSkuCount = pickSkuList.stream().distinct().collect(Collectors.toList()).size();
//            record.setPickNumCount(pickNumCount);
//            record.setPickSkuCount(pickSkuCount);
//            record.setOutNumCount(outNumCount);
//            record.setOutSkuCount(outSkuCount);
//        }
//        return PageFactory.createPageInfo(outStockViewPage);
//    }
//
//    @RequestMapping(value = "/outStockDetailView", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData outStockDetailView(@RequestBody DataStatisticsViewParam param) {
////        pickListsDetailService.lambdaQuery()
//        List<OutStockView> pickListsView = pickListsService.outStockView(param);
//        List<ProductionPickLists> pickLists = pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
//        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : pickListsCartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();
//        List<OutStockDetailView> results = new ArrayList<>();
//        List<OutStockDetailView> results2 = new ArrayList<>();
//
//
//        for (ProductionPickLists pickList : pickLists) {
//
//            for (ProductionPickListsCart cart : carts) {
//                if (cart.getPickListsId().equals(pickList.getPickListsId()) && cart.getStatus().equals(99)) {
//                    OutStockDetailView result = new OutStockDetailView();
//                    result.setSkuId(cart.getSkuId());
//                    if (param.getUserId().equals(pickList.getUserId())) {
//                        result.setOutNumCount(cart.getNumber());
//                    } else {
//                        result.setOutNumCount(0);
//                    }
//                    result.setBrandId(cart.getBrandId());
//                    results.add(result);
//                }
//            }
//        }
//        for (ProductionPickLists pickList : pickLists) {
//
//            for (ProductionPickListsCart cart : carts) {
//                if (cart.getPickListsId().equals(pickList.getPickListsId()) && cart.getCreateUser().equals(param.getUserId())) {
//                    OutStockDetailView result = new OutStockDetailView();
//                    result.setSkuId(cart.getSkuId());
//                    if (cart.getCreateUser().equals(param.getUserId())) {
//                        result.setPickNumCount(cart.getNumber());
//                    } else {
//                        result.setPickNumCount(0);
//                    }
//                    result.setBrandId(cart.getBrandId());
//                    results2.add(result);
//                }
//            }
//        }
//
//
//        List<OutStockDetailView> totalResults = new ArrayList<>();
//
//        results.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
//                        setOutNumCount(a.getOutNumCount() + b.getOutNumCount());
//                        setSkuId(a.getSkuId());
//                        setBrandId(a.getBrandId());
//                    }}).ifPresent(totalResults::add);
//                }
//        );
//        List<OutStockDetailView> totalResults2 = new ArrayList<>();
//
//        results2.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new OutStockDetailView() {{
//                        setPickNumCount(a.getPickNumCount() + b.getPickNumCount());
//                        setSkuId(a.getSkuId());
//                        setBrandId(a.getBrandId());
//                    }}).ifPresent(totalResults2::add);
//                }
//        );
//
//        for (OutStockDetailView totalResult : totalResults) {
//            for (OutStockDetailView outStockDetailView : totalResults2) {
//                if (totalResult.getSkuId().equals(outStockDetailView.getSkuId()) && totalResult.getBrandId().equals(outStockDetailView.getBrandId())) {
//                    totalResult.setPickNumCount(outStockDetailView.getPickNumCount());
//                    break;
//                }
//            }
//        }
//        List<SkuSimpleResult> skuSimpleResults = totalResults.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
//        List<BrandResult> brandResults = totalResults.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(totalResults.stream().map(OutStockDetailView::getSkuId).distinct().collect(Collectors.toList()));
//        for (OutStockDetailView totalResult : totalResults) {
//            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
//                if (totalResult.getSkuId().equals(skuSimpleResult.getSkuId())) {
//                    totalResult.setSkuResult(skuSimpleResult);
//                    break;
//                }
//            }
//            for (BrandResult brandResult : brandResults) {
//                if (totalResult.getBrandId().equals(brandResult.getBrandId())) {
//                    totalResult.setBrandResult(brandResult);
//                    break;
//                }
//            }
//        }
//
//
//        return ResponseData.success(totalResults);
//    }

    @RequestMapping(value = "/outStockOrderView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockOrderView(@RequestBody DataStatisticsViewParam param) {
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_TYPE:
                    return ResponseData.success(pickListsMapper.orderCountByType(param));
                case ORDER_STATUS:
                    return ResponseData.success(pickListsMapper.orderCountByStatus(param));
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/instockOrderView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData inStockOederView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_TYPE:
                    QueryChainWrapper<InstockOrder> typeWrapper = instockOrderService.query().select("count(instock_order_id) AS orderCount,create_user AS createUser,instock_type AS type").groupBy("instock_type");
                    if (BeanUtil.isNotEmpty(param.getBeginTime()) && BeanUtil.isNotEmpty(param.getEndTime())) {
                        typeWrapper.between("create_time", DateUtil.format(param.getBeginTime(), "yyyy-MM-dd") + " 00:00:00", DateUtil.format(param.getEndTime(), "yyyy-MM-dd") + " 23:59:59");
                    }
                    List<StockView> orderCountByTyper = instockOrderMapper.countOrderByType(param);

                    return ResponseData.success(orderCountByTyper);
                case ORDER_STATUS:
                    QueryChainWrapper<InstockOrder> statusWrapper = instockOrderService.query().select("count(instock_order_id) AS orderCount, status AS status ,create_user AS createUser").groupBy("status");
                    if (BeanUtil.isNotEmpty(param.getBeginTime()) && BeanUtil.isNotEmpty(param.getEndTime())) {
                        statusWrapper.between("create_time", DateUtil.format(param.getBeginTime(), "yyyy-MM-dd") + " 00:00:00", DateUtil.format(param.getEndTime(), "yyyy-MM-dd") + " 23:59:59");
                    }
                    List<StockView> orderCountByStatus = instockOrderMapper.countOrderByStatus(param);

                    return ResponseData.success(orderCountByStatus);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/outStockDetailView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockDetailView(@RequestBody DataStatisticsViewParam param) {
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_BY_CREATE_USER:
                    List<StockView> stockViewsByUser = pickListsMapper.orderCountByCreateUser(param);
                    List<UserResult> userResults = userService.getUserResultsByIds(stockViewsByUser.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : stockViewsByUser) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }

                    return ResponseData.success(stockViewsByUser);
                case ORDER_BY_DETAIL:
                    List<StockView> stockViews = pickListsMapper.orderDetailCountByCreateUser(param);
                    List<ProductionPickListsDetail> listsDetails = stockViews.size() == 0 ? new ArrayList<>() : pickListsDetailService.lambdaQuery().in(ProductionPickListsDetail::getPickListsId, stockViews.stream().map(StockView::getPickListsId).collect(Collectors.toList())).list();

                    List<Long> userIds = stockViews.stream().map(StockView::getCreateUser).distinct().collect(Collectors.toList());
                    List<UserResult> userResultsByIds = userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);


                    List<StockView> results = new ArrayList<>();
                    for (UserResult userResultsById : userResultsByIds) {
                        List<Long> skuIds = new ArrayList<>();
                        Integer number = 0;
                        for (ProductionPickListsDetail listsDetail : listsDetails) {
                            if (listsDetail.getReceivedNumber() > 0 && listsDetail.getCreateUser().equals(userResultsById.getUserId())) {
                                skuIds.add(listsDetail.getSkuId());
                                number += listsDetail.getReceivedNumber();
                            }
                        }
                        StockView stockView = new StockView();
                        stockView.setUserResult(userResultsById);
                        stockView.setOutSkuCount((int) skuIds.stream().distinct().count());
                        stockView.setOutNumCount(number);
                        results.add(stockView);

                    }
                    return ResponseData.success(results);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/outStockLogView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockLogView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_LOG:
                    List<StockView> logViews = outstockOrderMapper.groupByUser(param);
                    userResults = logViews.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logViews) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(logViews);
                case ORDER_LOG_DETAIL:

                    List<StockView> logDetailViews = outstockListingMapper.groupByUser(param);
                    userResults = logDetailViews.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logDetailViews) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(logDetailViews);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }


    @RequestMapping(value = "/instockLogView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockLogView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_LOG:
                    List<StockView> logViews = outstockOrderMapper.groupByUser(param);
                    userResults = logViews.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logViews) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(logViews);
                case ORDER_LOG_DETAIL:

                    List<StockView> logDetailViews = outstockListingMapper.groupByUser(param);
                    userResults = logDetailViews.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logDetailViews) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(logDetailViews);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/outstockOrderView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockOederView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_TYPE:
                    QueryChainWrapper<InstockOrder> typeWrapper = instockOrderService.query().select("count(instock_order_id) AS orderCount,create_user AS createUser").groupBy("type");
                    if (BeanUtil.isNotEmpty(param.getBeginTime()) && BeanUtil.isNotEmpty(param.getEndTime())) {
                        typeWrapper.between("create_time", DateUtil.format(param.getBeginTime(), "yyyy-MM-dd") + " 00:00:00", DateUtil.format(param.getEndTime(), "yyyy-MM-dd") + " 23:59:59");
                    }
                    List<InstockOrder> orderCountByUser = typeWrapper.list();
                    List<StockView> result = new ArrayList<>();

                    for (InstockOrder instockOrder : orderCountByUser) {
                        StockView stockView = new StockView();
//                        stockView.setOrderCount(instockOrder.getOrderCount());
                        stockView.setCreateUser(instockOrder.getCreateUser());
                        stockView.setType(instockOrder.getType());
                        result.add(stockView);
                    }
//                    userResults = userService.getUserResultsByIds(result.stream().map(StockView::getCreateUser).collect(Collectors.toList()));
//
//                    for (StockView stockView : result) {
//                        for (UserResult userResult : userResults) {
//                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
//                                stockView.setUserResult(userResult);
//                                break;
//                            }
//                        }
//                    }
                    return ResponseData.success(result);
                case ORDER_STATUS:
                    QueryChainWrapper<InstockOrder> statusWrapper = instockOrderService.query().select("count(instock_order_id) AS orderCount, type AS type ,create_user AS createUser").groupBy("status");
                    if (BeanUtil.isNotEmpty(param.getBeginTime()) && BeanUtil.isNotEmpty(param.getEndTime())) {
                        statusWrapper.between("create_time", DateUtil.format(param.getBeginTime(), "yyyy-MM-dd") + " 00:00:00", DateUtil.format(param.getEndTime(), "yyyy-MM-dd") + " 23:59:59");
                    }
                    List<InstockOrder> orderCountByStatus = statusWrapper.list();
                    List<StockView> stockViews = BeanUtil.copyToList(orderCountByStatus, StockView.class);

                    return ResponseData.success(stockViews);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/instockOrderCountViewByUser", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockOrderCountViewByUser(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_BY_CREATE_USER:
                    List<StockView> result = instockOrderMapper.countOrderByUser(param);


                    userResults = userService.getUserResultsByIds(result.stream().map(StockView::getCreateUser).collect(Collectors.toList()));

                    for (StockView stockView : result) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(result);
                case ORDER_BY_DETAIL:
                    LambdaQueryChainWrapper<InstockList> detailWrapper = instockListService.lambdaQuery();
                    if (BeanUtil.isNotEmpty(param.getBeginTime()) && BeanUtil.isNotEmpty(param.getEndTime())) {
                        detailWrapper.between(InstockList::getCreateTime, DateUtil.format(param.getBeginTime(), "yyyy-MM-dd") + " 00:00:00", DateUtil.format(param.getEndTime(), "yyyy-MM-dd") + " 23:59:59");
                    }
                    List<InstockList> detailCountByUser = detailWrapper.list();
                    userResults = userService.getUserResultsByIds(detailCountByUser.stream().map(InstockList::getCreateUser).distinct().collect(Collectors.toList()));

                    List<StockView> stockViews = new ArrayList<>();


                    for (UserResult userResult : userResults) {
                        List<Long> skuIds = new ArrayList<>();
                        int number = 0;
                        for (InstockList instockList : detailCountByUser) {
                            if (userResult.getUserId().equals(instockList.getCreateUser())) {
                                skuIds.add(instockList.getSkuId());
                                number += instockList.getNumber();
                            }
                        }
                        StockView stockView = new StockView();
                        stockView.setUserId(userResult.getUserId());
                        stockView.setUserResult(userResult);
                        stockView.setInNumCount(number);
                        stockView.setInSkuCount(skuIds.stream().distinct().collect(Collectors.toList()).size());
                        stockViews.add(stockView);
                    }

                    return ResponseData.success(stockViews);
                default:
                    break;
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/instockCountViewByMonth", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockCountViewByMonth(@RequestBody DataStatisticsViewParam param) {
        LambdaQueryChainWrapper<InstockOrder> instockOrderLambdaQueryChainWrapper = this.instockOrderService.lambdaQuery();
        instockOrderLambdaQueryChainWrapper.orderByDesc(InstockOrder::getCreateTime);
        instockOrderLambdaQueryChainWrapper.between(InstockOrder::getCreateTime, DateUtil.format(DateUtil.offsetMonth(new Date(), -11), "yyyy-MM" + "-1"), DateUtil.format(new Date(), "yyyy-MM-dd"));
        List<InstockOrder> instockOrders = instockOrderLambdaQueryChainWrapper.list();

        List<InstockListResult> instockLists = instockOrders.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(instockListService.lambdaQuery().in(InstockList::getInstockOrderId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), InstockListResult.class);

        List<AnomalyResult> instockErrors = instockOrders.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(anomalyService.lambdaQuery().eq(Anomaly::getType, "InstockError").in(Anomaly::getFormId, instockOrders.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), AnomalyResult.class);
        anomalyService.format(instockErrors);

        int[] monthList = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11};
        Date date = new Date();
        List<String> monthListStr = new ArrayList<>();
        for (int monthOffSet : monthList) {
            monthListStr.add(DateUtil.format(DateUtil.offsetMonth(date, monthOffSet), "yyyy-MM"));
        }

        Map<String, Integer> instockNumberByMonth = new HashMap<>();
        for (String month : monthListStr) {
            Integer number = instockNumberByMonth.get(month);
            if (ToolUtil.isEmpty(number)) {
                number = 0;
            }
            for (InstockListResult instockList : instockLists) {

                if (DateUtil.format(instockList.getCreateTime(), "yyyy-MM").equals(month)) {
                    number += Math.toIntExact(instockList.getInstockNumber());
                    ;
                }
            }
            instockNumberByMonth.put(month, number);
        }

        Map<String, Integer> errorNumberByMonth = new HashMap<>();

        for (String month : monthListStr) {
            int errorNumber = 0;
            if (BeanUtil.isNotEmpty(errorNumberByMonth.get(month))) {
                errorNumber = errorNumberByMonth.get(month);
            }
            for (AnomalyResult instockList : instockErrors) {
                if (DateUtil.format(instockList.getCreateTime(), "yyyy-MM").equals(month)) {
                    errorNumber += Math.toIntExact(instockList.getErrorNumber());
                }
            }
            errorNumberByMonth.put(month, errorNumber);
        }
        StockView stockView = new StockView();
        StockView logCount = instockLogDetailMapper.count(param);
        StockView errorCount = anomalyMapper.count(param);
        stockView.setErrorNumberByMonth(errorNumberByMonth);
        stockView.setNumberByMonth(instockNumberByMonth);
        if (BeanUtil.isNotEmpty(logCount)) {
            stockView.setInSkuCount(ToolUtil.isEmpty(logCount.getInSkuCount()) ? 0 : logCount.getInSkuCount());
            stockView.setInNumCount(ToolUtil.isEmpty(logCount.getInNumCount()) ? 0 : logCount.getInNumCount());
        }
        if (BeanUtil.isNotEmpty(errorCount)) {
            stockView.setErrorSkuCount(ToolUtil.isEmpty(errorCount.getErrorSkuCount()) ? 0 : errorCount.getErrorSkuCount());
            stockView.setErrorNumCount(ToolUtil.isEmpty(errorCount.getErrorNumCount()) ? 0 : errorCount.getErrorNumCount());

        }


        return ResponseData.success(stockView);
    }

    @RequestMapping(value = "/outstockCountViewByMonth", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outstockCountViewByMonth(@RequestBody DataStatisticsViewParam param) {
        param.setBeginTime(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -11)));
        param.setEndTime(DateUtil.date());
        List<StockView> stockViews = outstockListingMapper.groupByMonth(param);


        int[] monthList = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11};
        Date date = new Date();
        List<String> monthListStr = new ArrayList<>();
        for (int monthOffSet : monthList) {
            monthListStr.add(DateUtil.format(DateUtil.offsetMonth(date, monthOffSet), "yyyy-MM"));
        }

        Map<String, Integer> outstockNumberByMonth = new HashMap<>();
        for (String month : monthListStr) {

            for (StockView stockView : stockViews) {
                if (BeanUtil.isNotEmpty(stockView.getMonthOfYear()) && stockView.getMonthOfYear().equals(month)) {
                    outstockNumberByMonth.put(month, stockView.getOrderCount());
                }
            }
        }


        StockView stockView = new StockView();

        stockView.setNumberByMonth(outstockNumberByMonth);


        return ResponseData.success(stockView);
    }

    @RequestMapping(value = "/instockDetailBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockDetailBySpuClass(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SPU_CLASS:
                    return ResponseData.success(instockListMapper.groupBySpuClass(param));

                case TYPE:
                    return ResponseData.success(instockListMapper.groupByInstockType(param));

                case STOREHOUSE:
                    return ResponseData.success(instockListMapper.groupByStorehouse(param));
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/instockDetailByCustomer", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockDetailByCustomer(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SKU_COUNT:
                    return ResponseData.success(instockListMapper.groupByCustomerSku(param));

                case NUM_COUNT:
                    return ResponseData.success(instockListMapper.groupByCustomerNum(param));


            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/errorBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData errorBySpuClass(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/outBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outBySpuClass(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/outBytype", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outBytype(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/outByUser", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outByUser(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/outBystorehouse", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outBystorehouse(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/outStockDetailBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockDetailBySpuClass(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SPU_CLASS:
                    return ResponseData.success(outstockListingMapper.outBySpuClass(param));

                case TYPE:
                    return ResponseData.success(outstockListingMapper.outByType(param));

                case STOREHOUSE:
                    return ResponseData.success(outstockListingMapper.outByStoreHouse(param));
                case PICK_USER:
                    List<StockView> stockViews = outstockListingMapper.outByUser(param);
                    List<UserResult> userResultsByIds = userService.getUserResultsByIds(stockViews.stream().map(StockView::getUserId).collect(Collectors.toList()));
                    for (StockView stockView : stockViews) {
                        for (UserResult userResultsById : userResultsByIds) {

                            if (userResultsById.getUserId().equals(stockView.getUserId())) {
                                stockView.setUserResult(userResultsById);
                            }
                        }
                    }


                    return ResponseData.success(stockViews);
            }
        }
        return ResponseData.success();
    }

    @RequestMapping(value = "/outstockDetailByCustomer", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outstockDetailByCustomer(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SKU_COUNT:
                    return ResponseData.success(outstockListingMapper.outByCustomerSkuCount(param));

                case NUM_COUNT:
                    return ResponseData.success(outstockListingMapper.outByCustomerNumCount(param));
            }
        }
        return ResponseData.success();
    }

}

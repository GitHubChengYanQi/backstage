package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.mapper.StockDetailsMapper;
import cn.atsoft.dasheng.app.model.request.StockDetailView;
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
import cn.atsoft.dasheng.form.mapper.ProcessTaskMapper;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.TaskViewResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import static cn.atsoft.dasheng.erp.enums.ViewTypeEnum.SKU_COUNT;
import static cn.atsoft.dasheng.form.pojo.ProcessType.OUTSTOCK;

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
    @Autowired
    private StockDetailsMapper stockDetailsMapper;
    @Autowired
    private ProcessTaskMapper processTaskMapper;
    @Autowired
    private MaintenanceLogMapper maintenanceLogMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private AllocationLogMapper allocationLogMapper;


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


    @RequestMapping(value = "/outStockOrderView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockOrderView(@RequestBody DataStatisticsViewParam param) {
        Map<String, Object> result = new HashMap<>();

//        List<StockView> stockViews = ;
//        return ResponseData.success(stockViews);
//        return ResponseData.success();

        result.put("orderCountByType", pickListsMapper.orderCountByType(param));
        result.put("orderCountByStatus", pickListsMapper.orderCountByStatus(param));
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/instockOrderView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData inStockOederView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        List<StockView> orderCountByTyper = instockOrderMapper.countOrderByType(param);
        result.put("orderCountByTyper", orderCountByTyper);


        List<StockView> orderCountByStatus = instockOrderMapper.countOrderByStatus(param);
        result.put("orderCountByStatus", orderCountByStatus);

        return ResponseData.success(result);

    }

    @RequestMapping(value = "/instockLogView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockLogView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        Page<StockView> logViews = outstockOrderMapper.groupByUser(PageFactory.defaultPage(), param);
        userResults = logViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
        for (StockView stockView : logViews.getRecords()) {
            for (UserResult userResult : userResults) {
                if (stockView.getCreateUser().equals(userResult.getUserId())) {
                    stockView.setUserResult(userResult);
                    break;
                }
            }
        }
        result.put("logViews", logViews);

        Page<StockView> logDetailViews = outstockListingMapper.groupByUser(PageFactory.defaultPage(), param);
        userResults = logDetailViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
        for (StockView stockView : logDetailViews.getRecords()) {
            for (UserResult userResult : userResults) {
                if (stockView.getCreateUser().equals(userResult.getUserId())) {
                    stockView.setUserResult(userResult);
                    break;
                }
            }
        }
        result.put("logDetailViews", logDetailViews);
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/outStockDetailView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outStockDetailView(@RequestBody DataStatisticsViewParam param) {
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_TYPE:
                    break;
                case ORDER_STATUS:
                    break;
                case ORDER_BY_CREATE_USER:
                    Page<StockView> stockViewsByUser = pickListsMapper.orderCountByCreateUser(PageFactory.defaultPage(), param);
                    List<UserResult> userResults = stockViewsByUser.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(stockViewsByUser.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
//                    List<ActivitiProcessTaskResult> activitiProcessTaskResults = BeanUtil.copyToList(activitiProcessTaskService.lambdaQuery().in(ActivitiProcessTask::getCreateUser, userResults.stream().map(UserResult::getUserId).collect(Collectors.toList())).eq(ActivitiProcessTask::getType, OUTSTOCK.getName()).list(), ActivitiProcessTaskResult.class);
//                    activitiProcessTaskService.format(activitiProcessTaskResults);

                    for (StockView stockView : stockViewsByUser.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
//                        List<ActivitiProcessTaskResult> taskResults = new ArrayList<>();
//                        for (ActivitiProcessTaskResult activitiProcessTaskResult : activitiProcessTaskResults) {
//                            if (stockView.getCreateUser().equals(activitiProcessTaskResult.getUserId())){
//                                taskResults.add(activitiProcessTaskResult);
//                            }
//                        }
//                        stockView.setTaskResults(taskResults);
                    }

                    return PageFactory.createPageInfo(stockViewsByUser);
                case ORDER_BY_DETAIL:
                    Page<StockView> stockViews = pickListsMapper.orderDetailCountByCreateUser(PageFactory.defaultPage(), param);
                    List<StockView> details = pickListsDetailService.getUserSkuAndNumbers(param);


                    List<SkuSimpleResult> skuResult = details.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(details.stream().map(StockView::getSkuId).distinct().collect(Collectors.toList()));
//
                    List<UserResult> userResultsByIds = userService.getUserResultsByIds(stockViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));

                    for (StockView detail : details) {
                        for (SkuSimpleResult skuSimpleResult : skuResult) {
                            if (detail.getSkuId().equals(skuSimpleResult.getSkuId())) {
                                detail.setSkuResult(skuSimpleResult);
                                break;
                            }
                        }
                    }
                    for (StockView record : stockViews.getRecords()) {
                        for (UserResult userResultsById : userResultsByIds) {
                            if (record.getCreateUser().equals(userResultsById.getUserId())) {
                                record.setUserResult(userResultsById);
                                break;
                            }
                        }
                        List<StockView.SkuAndNumber> skuAndNumbers = new ArrayList<>();
                        for (StockView detail : details) {
                            if (record.getCreateUser().equals(detail.getCreateUser())) {
                                StockView.SkuAndNumber skuAndNumber = new StockView.SkuAndNumber();
                                skuAndNumber.setSkuId(detail.getSkuId());
                                skuAndNumber.setSkuResult(detail.getSkuResult());
                                skuAndNumber.setNumber(detail.getOutNumCount());
                                skuAndNumbers.add(skuAndNumber);
                            }
                        }
                        record.setSkuAndNumbers(skuAndNumbers);
                    }


                    return PageFactory.createPageInfo(stockViews);
                case ORDER_LOG:
                    break;
                case ORDER_LOG_DETAIL:
                    break;
                case SPU_CLASS:
                    break;
                case STOREHOUSE:
                    break;
                case TYPE:
                    break;
                case SKU_COUNT:
                    break;
                case NUM_COUNT:
                    break;
                case PICK_USER:
                    break;
                default:
                    break;
            }
        }
        return new PageInfo();
    }

    @RequestMapping(value = "/outStockLogView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockLogView(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Page<StockView> logViews = outstockOrderMapper.groupByUser(PageFactory.defaultPage(), param);
        userResults = logViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
        for (StockView stockView : logViews.getRecords()) {
            for (UserResult userResult : userResults) {
                if (stockView.getCreateUser().equals(userResult.getUserId())) {
                    stockView.setUserResult(userResult);
                    break;
                }
            }
        }
        result.put("logViews", logViews);


        Page<StockView> logDetailViews = outstockListingMapper.groupByUser(PageFactory.defaultPage(), param);
        userResults = logDetailViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
        for (StockView stockView : logDetailViews.getRecords()) {
            for (UserResult userResult : userResults) {
                if (stockView.getCreateUser().equals(userResult.getUserId())) {
                    stockView.setUserResult(userResult);
                    break;
                }
            }
        }
        result.put("logDetailViews", logDetailViews);


        return ResponseData.success(result);
    }

    @RequestMapping(value = "/outStockLogViewDetail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outStockLogViewDetail(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_LOG:
                    Page<StockView> logViews = outstockOrderMapper.groupByUser(PageFactory.defaultPage(), param);
                    userResults = logViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logViews.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return ResponseData.success(logViews);
                case ORDER_LOG_DETAIL:

                    Page<StockView> logDetailViews = outstockListingMapper.groupByUser(PageFactory.defaultPage(), param);
                    userResults = logDetailViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logDetailViews.getRecords()) {
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

    @RequestMapping(value = "/instockLogViewDetail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo instockLogViewDetail(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_LOG:
                    Page<StockView> logViews = outstockOrderMapper.groupByUser(PageFactory.defaultPage(), param);
                    userResults = logViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logViews.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return PageFactory.createPageInfo(logViews);
                case ORDER_LOG_DETAIL:

                    Page<StockView> logDetailViews = outstockListingMapper.groupByUser(PageFactory.defaultPage(), param);
                    userResults = logDetailViews.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(logDetailViews.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));
                    for (StockView stockView : logDetailViews.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return PageFactory.createPageInfo(logDetailViews);
                default:
                    break;
            }
        }
        return null;
    }


    @RequestMapping(value = "/instockOrderCountViewByUser", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockOrderCountViewByUser(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        Page<StockView> countOrderByUser = instockOrderMapper.countOrderByUser(PageFactory.defaultPage(), param);
        userResults = countOrderByUser.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(countOrderByUser.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));

        for (StockView stockView : countOrderByUser.getRecords()) {
            for (UserResult userResult : userResults) {
                if (stockView.getCreateUser().equals(userResult.getUserId())) {
                    stockView.setUserResult(userResult);
                    break;
                }
            }
        }
        result.put("countOrderByUser", countOrderByUser);
        Page<StockView> sumOrderByUser = instockOrderMapper.sumOrderByUser(PageFactory.defaultPage(), param);
        userResults = sumOrderByUser.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(sumOrderByUser.getRecords().stream().map(StockView::getUserId).distinct().collect(Collectors.toList()));

        for (StockView record : sumOrderByUser.getRecords()) {
            for (UserResult userResult : userResults) {
                if (record.getUserId().equals(userResult.getUserId())) {
                    record.setUserResult(userResult);
                }
            }
        }
        result.put("sumOrderByUser", sumOrderByUser);
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/instockOrderCountViewByUserDetail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo instockOrderCountViewByUserDetail(@RequestBody DataStatisticsViewParam param) {
        List<UserResult> userResults = new ArrayList<>();
        if (param.getSearchType() != null) {
            switch (param.getSearchType()) {
                case ORDER_BY_CREATE_USER:
                    Page<StockView> result = instockOrderMapper.countOrderByUser(PageFactory.defaultPage(), param);
                    userResults = result.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(result.getRecords().stream().map(StockView::getCreateUser).collect(Collectors.toList()));

                    for (StockView stockView : result.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (stockView.getCreateUser().equals(userResult.getUserId())) {
                                stockView.setUserResult(userResult);
                                break;
                            }
                        }
                    }
                    return PageFactory.createPageInfo(result);
                case ORDER_BY_DETAIL:
                    Page<StockView> result2 = instockOrderMapper.sumOrderByUser(PageFactory.defaultPage(), param);
                    userResults = result2.getRecords().size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(result2.getRecords().stream().map(StockView::getUserId).distinct().collect(Collectors.toList()));

                    for (StockView record : result2.getRecords()) {
                        for (UserResult userResult : userResults) {
                            if (record.getUserId().equals(userResult.getUserId())) {
                                record.setUserResult(userResult);
                            }
                        }
                    }
                    return PageFactory.createPageInfo(result2);
                default:
                    break;
            }
        }
        return null;
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


//        int[] monthList = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11};
//        Date date = new Date();
//        List<String> monthListStr = new ArrayList<>();
//        for (int monthOffSet : monthList) {
//            monthListStr.add(DateUtil.format(DateUtil.offsetMonth(date, monthOffSet), "yyyy-MM"));
//        }
//
//        Map<String, Integer> outstockNumberByMonth = new HashMap<>();
//        for (String month : monthListStr) {

//            for (StockView stockView : stockViews) {
//                if (BeanUtil.isNotEmpty(stockView.getMonthOfYear()) && stockView.getMonthOfYear().equals(month)) {
//                    outstockNumberByMonth.put(month, stockView.getOrderCount());
//                }
//            }
//        }


//        StockView stockView = new StockView();
//
//        stockView.setNumberByMonth(outstockNumberByMonth);


        return ResponseData.success(stockViews);
    }

    @RequestMapping(value = "/instockDetailBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo instockDetailBySpuClass(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SPU_CLASS:
                    return PageFactory.createPageInfo(instockListMapper.groupBySpuClass(PageFactory.defaultPage(), param));

                case TYPE:
                    return PageFactory.createPageInfo(instockListMapper.groupByInstockType(PageFactory.defaultPage(), param));

                case STOREHOUSE:
                    return PageFactory.createPageInfo(instockListMapper.groupByStorehouse(PageFactory.defaultPage(), param));
            }
        }
        return new PageInfo();
    }

    @RequestMapping(value = "/instockDetailByCustomer", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo instockDetailByCustomer(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SKU_COUNT:
                    return PageFactory.createPageInfo(instockListMapper.groupByCustomerSku(PageFactory.defaultPage(), param));

                case NUM_COUNT:
                    return PageFactory.createPageInfo(instockListMapper.groupByCustomerNum(PageFactory.defaultPage(), param));


            }
        }
        return null;
    }

    @RequestMapping(value = "/errorBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData errorBySpuClass(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(anomalyMapper.countErrorByOrderType(param));
    }

    @RequestMapping(value = "/instockCustomer", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockCustomer(@RequestBody DataStatisticsViewParam param) {

        return ResponseData.success(instockOrderMapper.instockCustomer(param));
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

    @RequestMapping(value = "/outBySku", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outBySku(@RequestBody DataStatisticsViewParam param) {
        Page<StockView.SkuAndNumber> stockViews = outstockListingMapper.outBySku(PageFactory.defaultPage(), param);
        List<Long> skuIds = new ArrayList<>();
        for (StockView.SkuAndNumber record : stockViews.getRecords()) {
            skuIds.add(record.getSkuId());
        }


        List<SkuSimpleResult> skuResult = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (StockView.SkuAndNumber record : stockViews.getRecords()) {
            for (SkuSimpleResult skuSimpleResult : skuResult) {
                if (record.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    record.setSkuResult(skuSimpleResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(stockViews);
    }

    @RequestMapping(value = "/outBySkuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData outBySkuClass(@RequestBody DataStatisticsViewParam param) {
        List<StockView.SkuAndNumber> stockViews = outstockListingMapper.outBySpuClass(param);
        List<Long> skuIds = new ArrayList<>();
        for (StockView.SkuAndNumber record : stockViews) {
            skuIds.add(record.getSkuId());
        }


        List<SkuSimpleResult> skuResult = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (StockView.SkuAndNumber record : stockViews) {
            for (SkuSimpleResult skuSimpleResult : skuResult) {
                if (record.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    record.setSkuResult(skuSimpleResult);
                    break;
                }
            }
        }
        return ResponseData.success(stockViews);
    }

    @RequestMapping(value = "/outStockDetailBySpuClass", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outStockDetailBySpuClass(@RequestBody DataStatisticsViewParam param) {
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            Page<Object> objectPage = PageFactory.defaultPage();
            switch (param.getSearchType()) {
                case SPU_CLASS:
                    return PageFactory.createPageInfo(outstockListingMapper.outBySpuClassCount(objectPage, param));

                case TYPE:
                    return PageFactory.createPageInfo(outstockListingMapper.outByType(objectPage, param));

                case STOREHOUSE:
                    return PageFactory.createPageInfo(outstockListingMapper.outByStoreHouse(objectPage, param));
                case PICK_USER:
                    IPage<StockView> stockViews = outstockListingMapper.outByUser(objectPage, param);
                    List<UserResult> userResultsByIds = userService.getUserResultsByIds(stockViews.getRecords().stream().map(StockView::getUserId).collect(Collectors.toList()));
                    for (StockView stockView : stockViews.getRecords()) {
                        for (UserResult userResultsById : userResultsByIds) {

                            if (userResultsById.getUserId().equals(stockView.getUserId())) {
                                stockView.setUserResult(userResultsById);
                            }
                        }
                    }


                    return PageFactory.createPageInfo(stockViews);
            }
        }
        return new PageInfo();
    }

    @RequestMapping(value = "/outstockDetailByCustomer", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outstockDetailByCustomer(@RequestBody DataStatisticsViewParam param) {
        List<SkuSimpleResult> skuResults = new ArrayList<>();
        Page<StockView> stockViewPage = new Page<>();
        if (ToolUtil.isNotEmpty(param.getSearchType())) {
            switch (param.getSearchType()) {
                case SKU_COUNT:
                    stockViewPage = outstockListingMapper.outByCustomerSkuCount(PageFactory.defaultPage(), param);
                    break;
                case NUM_COUNT:

                    stockViewPage = outstockListingMapper.outByCustomerNumCount(PageFactory.defaultPage(), param);
                    break;
            }
            List<StockView> details = outstockListingMapper.groupByUserAndSku(param);
            skuResults = skuService.simpleFormatSkuResult(details.stream().map(StockView::getSkuId).distinct().collect(Collectors.toList()));
            for (StockView record : stockViewPage.getRecords()) {
                List<StockView.SkuAndNumber> skuAndNumbers = new ArrayList<>();
                for (StockView detail : details) {
                    if (detail.getCreateUser().equals(record.getCreateUser())) {
                        StockView.SkuAndNumber skuAndNumber = new StockView.SkuAndNumber();
                        skuAndNumber.setNumber(detail.getOutNumCount());
                        skuAndNumber.setSkuId(detail.getSkuId());
                        for (SkuSimpleResult skuResult : skuResults) {
                            if (detail.getSkuId().equals(skuResult.getSkuId())) {
                                skuAndNumber.setSkuResult(skuResult);
                                break;
                            }
                        }
                        skuAndNumbers.add(skuAndNumber);
                    }
                }
                record.setSkuAndNumbers(skuAndNumbers);
            }
        }
        return PageFactory.createPageInfo(stockViewPage);
    }

    @RequestMapping(value = "/instockDetail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockDetail(@RequestBody DataStatisticsViewParam param) {
        List<StockView.SkuAndNumber> skuAndNumbers = instockOrderMapper.instockDetails(param);
        List<SkuSimpleResult> skuResults = skuService.simpleFormatSkuResult(skuAndNumbers.stream().map(StockView.SkuAndNumber::getSkuId).distinct().collect(Collectors.toList()));
        for (StockView.SkuAndNumber skuAndNumber : skuAndNumbers) {
            for (SkuSimpleResult skuResult : skuResults) {
                if (skuAndNumber.getSkuId().equals(skuResult.getSkuId())) {
                    skuAndNumber.setSkuResult(skuResult);
                    break;
                }
            }
        }
        return ResponseData.success(skuAndNumbers);


    }

    @RequestMapping(value = "/instockLogs", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockLogs(@RequestBody DataStatisticsViewParam param) {
        List<StockView.SkuAndNumber> skuAndNumbers = instockLogDetailMapper.instockLogs(param);
        List<Long> skuIds = new ArrayList<>();
        for (StockView.SkuAndNumber skuAndNumber : skuAndNumbers) {
            skuIds.add(skuAndNumber.getSkuId());
        }
//        skuAndNumbers.size() == 0 ? new ArrayList<>() : skuAndNumbers.stream().map(StockView.SkuAndNumber::getSkuId).distinct().collect(Collectors.toList());
        List<SkuSimpleResult> skuResults = skuService.simpleFormatSkuResult(skuIds);
        for (StockView.SkuAndNumber skuAndNumber : skuAndNumbers) {
            for (SkuSimpleResult skuResult : skuResults) {
                if (skuAndNumber.getSkuId().equals(skuResult.getSkuId())) {
                    skuAndNumber.setSkuResult(skuResult);
                    break;
                }
            }
        }

        return ResponseData.success(skuAndNumbers);


    }

    @RequestMapping(value = "/instockOrders", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData instockOrders(@RequestBody DataStatisticsViewParam param) {
        List<InstockOrder> list = instockOrderService.lambdaQuery().eq(InstockOrder::getCreateUser, param.getUserId()).eq(InstockOrder::getDisplay, 1).list();
        List<ActivitiProcessTask> instock = list.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.lambdaQuery().eq(ActivitiProcessTask::getCreateUser, param.getUserId()).eq(ActivitiProcessTask::getType, "INSTOCK").in(ActivitiProcessTask::getFormId, list).list();
        List<ActivitiProcessTaskResult> taskResults = BeanUtil.copyToList(instock, ActivitiProcessTaskResult.class);
        activitiProcessTaskService.format(taskResults);
        return ResponseData.success(taskResults);
    }

    @RequestMapping(value = "/outStockViewDetail", method = RequestMethod.POST)
    @ApiOperation("出库汇总详情")
    public PageInfo outStockViewDetail(@RequestBody DataStatisticsViewParam param) {
        Page<StockView.SkuAndNumber> skuAndNumberPage = outstockListingMapper.outBySku(PageFactory.defaultPage(), param);
        List<Long> skuId = skuAndNumberPage.getRecords().stream().map(StockView.SkuAndNumber::getSkuId).distinct().collect(Collectors.toList());
        List<SkuSimpleResult> skuResult = skuId.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuId);
        for (StockView.SkuAndNumber record : skuAndNumberPage.getRecords()) {
            for (SkuSimpleResult skuSimpleResult : skuResult) {
                if (record.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    record.setSkuResult(skuSimpleResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(skuAndNumberPage);
    }

    //============================================================================================================================================================================================================================================================================================
    //综合统计


    @RequestMapping(value = "/stockNumberView", method = RequestMethod.POST)
    @ApiOperation("库存数量排行")
    public PageInfo<StockDetailView> stockNumberView(@RequestBody DataStatisticsViewParam param) {
        Page<Object> objectPage = PageFactory.defaultPage();
        return PageFactory.createPageInfo(stockDetailsMapper.dataStatisticsView(objectPage, param));
    }

    @RequestMapping(value = "/dataStatisticsViewDetail", method = RequestMethod.POST)
    @ApiOperation("库存数量排行")
    public ResponseData dataStatisticsViewDetail(@RequestBody DataStatisticsViewParam param) {
        List<StockDetailView> stockDetailViews = stockDetailsMapper.dataStatisticsViewDetail(param);
        List<Long> skuIds = stockDetailViews.stream().map(StockDetailView::getSkuId).collect(Collectors.toList());
        List<SkuSimpleResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (StockDetailView stockDetailView : stockDetailViews) {
            for (SkuSimpleResult skuResult : skuResults) {
                if (stockDetailView.getSkuId().equals(skuResult.getSkuId())) {
                    stockDetailView.setSkuSimpleResult(skuResult);
                    break;
                }
            }
        }
        return ResponseData.success(stockDetailViews);
    }

    @RequestMapping(value = "/stockNumberCycle", method = RequestMethod.POST)
    @ApiOperation("库存周期占比")
    public ResponseData stockNumberCycle(@RequestBody DataStatisticsViewParam param) {
        Map<String, Object> result = new HashMap<>();
        param.setCycle("1month");
        result.put("1month", this.stockDetailsMapper.stockNumberCycle(param));
        param.setCycle("1month-3month");
        result.put("1month-3month", this.stockDetailsMapper.stockNumberCycle(param));
        param.setCycle("3month-6month");
        result.put("3month-6month", this.stockDetailsMapper.stockNumberCycle(param));
        param.setCycle("after6month");
        result.put("after6month", this.stockDetailsMapper.stockNumberCycle(param));
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/stockNumberCycleDetail", method = RequestMethod.POST)
    @ApiOperation("库存周期占比详情")
    public ResponseData stockNumberCycleDetail(@RequestBody DataStatisticsViewParam param) {
        List<StockDetailView> stockDetailViews = this.stockDetailsMapper.stockNumberCycleDetail(param);
        List<Long> skuIds = stockDetailViews.stream().map(StockDetailView::getSkuId).collect(Collectors.toList());
        List<SkuSimpleResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (StockDetailView stockDetailView : stockDetailViews) {
            for (SkuSimpleResult skuResult : skuResults) {
                if (stockDetailView.getSkuId().equals(skuResult.getSkuId())) {
                    stockDetailView.setSkuSimpleResult(skuResult);
                    break;
                }
            }
        }
        return ResponseData.success(stockDetailViews);
    }

    @RequestMapping(value = "/taskNumberView", method = RequestMethod.POST)
    @ApiOperation("任务统计")
    public ResponseData taskNumberView(@RequestBody DataStatisticsViewParam param) {
        Page<Object> objectPage = PageFactory.defaultPage();
        Map<String, Object> result = new HashMap<>();
        result.put("taskNumberView", PageFactory.createPageInfo(processTaskMapper.taskNumberView(objectPage, param)));
        result.put("taskTypeView", PageFactory.createPageInfo(processTaskMapper.taskTypeView(objectPage, param)));
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/taskUserView", method = RequestMethod.POST)
    @ApiOperation("任务统计")
    public ResponseData taskUserView(@RequestBody DataStatisticsViewParam param) {
        List<TaskViewResult> taskViewResults = processTaskMapper.taskUserView(param);
        List<Long> userIds = taskViewResults.stream().map(TaskViewResult::getCreateUser).distinct().collect(Collectors.toList());
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        for (TaskViewResult taskViewResult : taskViewResults) {
            for (UserResult userResult : userResultsByIds) {
                if (taskViewResult.getCreateUser().equals(userResult.getUserId())) {
                    taskViewResult.setUserResult(userResult);
                    break;
                }
            }
        }
        return ResponseData.success(taskViewResults);
    }

    @RequestMapping(value = "/taskLogUserView", method = RequestMethod.POST)
    @ApiOperation("任务统计")
    public ResponseData taskLogUserView(@RequestBody DataStatisticsViewParam param) {
//        List<StockView> stockViews = outstockOrderMapper.groupByUserList(param);
        List<MaintenanceLogResult> maintenanceLogResults = maintenanceLogMapper.groupByUserList(param);
        List<StockLogResult> stockLogResults = stockLogMapper.viewByUserAndType(param);
        List<Long> userIds = new ArrayList<>();
        List<AllocationLogResult> allocationLogResults = allocationLogMapper.countByCreateUser(param);


//        userIds.addAll(stockViews.stream().map(StockView::getCreateUser).distinct().collect(Collectors.toList()));
        userIds.addAll(maintenanceLogResults.stream().map(MaintenanceLogResult::getCreateUser).distinct().collect(Collectors.toList()));
        userIds.addAll(stockLogResults.stream().map(StockLogResult::getCreateUser).distinct().collect(Collectors.toList()));
        if (userIds.size() > 0) {
            Page<UserResult> userResultPage = userService.userResultPageList(new UserParam() {{
                setUserIds(userIds.stream().distinct().collect(Collectors.toList()));
            }});
            List<Map<String, Object>> results = new ArrayList<>();
            for (UserResult record : userResultPage.getRecords()) {
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(record);

                for (MaintenanceLogResult maintenanceLogResult : maintenanceLogResults) {
                    if (record.getUserId().equals(maintenanceLogResult.getCreateUser())) {
                        stringObjectMap.put("maintenanceCount", maintenanceLogResult.getNumber());
                    }
                }
                for (AllocationLogResult allocationLogResult : allocationLogResults) {
                    if (record.getUserId().equals(allocationLogResult.getCreateUser())) {
                        stringObjectMap.put("allocationCount", allocationLogResult.getNumber());

                    }
                    for (StockLogResult stockLogResult : stockLogResults) {
                        if (record.getUserId().equals(stockLogResult.getCreateUser())) {
                            switch (stockLogResult.getSource()) {
                                case "instock":
                                    stringObjectMap.put("instockCount", stockLogResult.getNumber());
                                    break;
                                case "outstock":
                                    stringObjectMap.put("outsockCount", stockLogResult.getNumber());
                                    break;
                                case "inventory":
                                    stringObjectMap.put("inventoryCount", stockLogResult.getNumber());
                                    break;
                            }
                        }
                    }

                }
                results.add(stringObjectMap);

            }
            return ResponseData.success(results);
        }
        return ResponseData.success();
    }

}

package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.request.InstockView;
import cn.atsoft.dasheng.app.model.request.OutStockView;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
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
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
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
    public PageInfo billPageList(@RequestBody DataStatisticsViewParam param) {

        Page<InstockView> longPageInfo = customerService.customIdListFromInStockOrder(param);
        List<CustomerResult> customerResults = longPageInfo.getRecords().size() == 0 ? new ArrayList<>() : customerService.getResults(longPageInfo.getRecords().stream().map(InstockView::getCustomerId).collect(Collectors.toList()));
        List<InstockOrder> list = longPageInfo.getRecords().size() == 0 ? new ArrayList<>() : instockOrderService.lambdaQuery().in(InstockOrder::getCustomerId, longPageInfo.getRecords().stream().map(InstockView::getCustomerId).collect(Collectors.toList())).list();
        List<InstockLogDetail> logDetails = (list.size() == 0) ? new ArrayList<>() : instockLogDetailService.lambdaQuery().in(InstockLogDetail::getInstockOrderId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list();
        List<InstockList> instockLists = list.size() == 0 ? new ArrayList<>() : instockListService.lambdaQuery().in(InstockList::getInstockOrderId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).eq(InstockList::getAnomalyHandle,"stopInStock").list();
        List<AnomalyResult> instockErrors = BeanUtil.copyToList(anomalyService.lambdaQuery().eq(Anomaly::getType, "InstockError").in(Anomaly::getFormId, list.stream().map(InstockOrder::getInstockOrderId).collect(Collectors.toList())).list(), AnomalyResult.class);
        anomalyService.format(instockErrors);



        for (InstockView record : longPageInfo.getRecords()) {
            for (CustomerResult customerResult : customerResults) {
                if (record.getCustomerId().equals(customerResult.getCustomerId())) {
                    record.setCustomerResult(customerResult);
                }
            }

            List<InstockLogDetail> detailResults = new ArrayList<>();
            List<InstockList> lists = new ArrayList<>();
            for (InstockOrder instockOrder : list) {
                if (record.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (InstockLogDetail detail : logDetails) {
                        if (detail.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            detailResults.add(detail);
                        }
                    }

                    for (InstockList instockList : instockLists) {
                        if (instockList.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            lists.add(instockList);
                        }
                    }
                }
                for (AnomalyResult instockError : instockErrors) {

                }
            }
            record.setInstockLists(lists);
            record.setInstockLogDetails(detailResults);
            Integer logSkuCount = detailResults.stream().map(InstockLogDetail::getSkuId).distinct().collect(Collectors.toList()).size();
            Integer detailSkuCount = lists.stream().map(InstockList::getSkuId).distinct().collect(Collectors.toList()).size();
            int logNumberCount = 0;
            for (InstockLogDetail detailResult : detailResults) {
                logNumberCount += detailResult.getNumber();
            }
            int detailNumberCount = 0;
            for (InstockList listResult : lists) {
                detailNumberCount += listResult.getNumber();
            }
            int errorSkuCount = instockErrors.stream().map(AnomalyResult::getSkuId).collect(Collectors.toList()).size();
            int errorNumberCount = 0;
            for (AnomalyResult instockError : instockErrors) {
                errorNumberCount += instockError.getErrorNumber();
            }



            record.setErrorNumberCount(errorNumberCount);
            record.setErrorSkuCount(errorSkuCount);
            record.setDetailSkuCount(detailSkuCount);
            record.setDetailNumberCount(detailNumberCount);
            record.setLogSkuCount(logSkuCount);
            record.setLogNumberCount(logNumberCount);
        }

        return PageFactory.createPageInfo(longPageInfo);
    }

    @RequestMapping(value = "/outstockView", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo outStockView(@RequestBody DataStatisticsViewParam param) {
        Page<OutStockView> outStockViewPage = pickListsService.outStockView(param);
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(outStockViewPage.getRecords().stream().map(OutStockView::getUserId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickListsCartService.lambdaQuery().eq(ProductionPickListsCart::getPickListsId, outStockViewPage.getRecords().stream().map(OutStockView::getPickListsId).collect(Collectors.toList())).ne(ProductionPickListsCart::getStatus, -1).list();

        return PageFactory.createPageInfo(new Page<>());
    }

}

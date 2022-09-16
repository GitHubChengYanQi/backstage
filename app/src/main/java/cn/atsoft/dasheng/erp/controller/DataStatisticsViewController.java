package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.AllocationLogResult;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @RequestMapping(value = "/billPageList", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo<Object> billPageList(@RequestBody DataStatisticsViewParam param) {
        PageInfo<Object> result = new PageInfo<>();
        if (ToolUtil.isEmpty(param.getType())) {
            throw new ServiceException(500, "请传入参数");
        }
        List<String> times = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getDateParams())) {
            for (Date dateParam : param.getDateParams()) {
                String timeStr = DateUtil.format(dateParam, "yyyy-MM-dd HH:mm:ss");
                times.add(timeStr);
            }
        }
        switch (param.getType()) {
            case "instockLog":
                PageInfo<InstockReceiptResult> pageBySpec = instockReceiptService.findPageBySpec(new InstockReceiptParam() {{
                    if (ToolUtil.isNotEmpty(param.getCreateUser())) {
                        setCreateUser(param.getCreateUser());
                    }
                    setTimes(times);
                }});
                ToolUtil.copyProperties(pageBySpec, result);
                break;
            case "maintenanceLog":
                PageInfo<MaintenanceLogResult> pageBySpec1 = maintenanceLogService.findPageBySpec(new MaintenanceLogParam() {{
                    if (ToolUtil.isNotEmpty(param.getCreateUser())) {
                        setCreateUser(param.getCreateUser());
                    }
                    setTimes(times);
                }});
                ToolUtil.copyProperties(pageBySpec1, result);
                break;
            case "outstockLog":
                PageInfo<OutstockOrderResult> pageBySpec2 = outstockOrderService.findPageBySpec(new OutstockOrderParam() {{
                    if (ToolUtil.isNotEmpty(param.getCreateUser())) {
                        setCreateUser(param.getCreateUser());
                    }
                    setTimes(times);
                }}, null);
                ToolUtil.copyProperties(pageBySpec2, result);
                break;
            case "inventoryLog":
                PageInfo pageBySpec3 = inventoryService.findPageBySpec(new InventoryParam() {{
                    if (ToolUtil.isNotEmpty(param.getCreateUser())) {
                        setCreateUser(param.getCreateUser());
                    }
                    setComplete(99);
                    setTimes(times);
                }});
                ToolUtil.copyProperties(pageBySpec3, result);
                break;
            case "allocationLog":
                PageInfo<AllocationLogResult> pageBySpec4 = allocationLogService.findPageBySpec(new AllocationLogParam() {{
                    if (ToolUtil.isNotEmpty(param.getCreateUser())) {
                        setCreateUser(param.getCreateUser());
                    }
                    setTimes(times);
                }});
                ToolUtil.copyProperties(pageBySpec4, result);
                break;
            case "anomaly":
                PageInfo<AnomalyOrderResult> pageBySpec5 = anomalyOrderService.findPageBySpec(new AnomalyOrderParam());
                ToolUtil.copyProperties(pageBySpec5, result);
                break;
        }
        return result;
    }


}

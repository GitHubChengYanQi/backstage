package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.Excel.service.InstockViewExcel;
import cn.atsoft.dasheng.Excel.service.OutStockViewExcel;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.service.DynamicService;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.service.AnnouncementsService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.MaintenanceService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static cn.atsoft.dasheng.enmu.MicroServiceType.INIT;

@Service
public class MicroMessageService {


    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private OutstockOrderService outstockOrderService;

    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    @Autowired
    private ProductionCardService productionCardService;

    @Autowired
    private ProductionPickListsService productionPickListsService;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private AnnouncementsService announcementsService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private InstockViewExcel instockViewExcel;

    @Autowired
    private OutStockViewExcel outStockViewExcel;
//
    @Autowired
    private InitTenantService initTenantService;


    public void microServiceDo(MicroServiceEntity microServiceEntity) throws IOException {
        switch (microServiceEntity.getType()) {
            case CONTRACT:

                switch (microServiceEntity.getOperationType()) {
                    case SAVE:
                        //保存

                        break;
                    case ADD:
                        ContractParam contractParam = JSON.parseObject(microServiceEntity.getObject().toString(), ContractParam.class);
                        break;
                }
                break;

            case PRODUCTION_PLAN:
                ProductionPlanParam productionPlanParam = JSON.parseObject(microServiceEntity.getObject().toString(), ProductionPlanParam.class);
                switch (microServiceEntity.getOperationType()) {
                    case ADD:
                        productionPlanService.add(productionPlanParam);
                        break;
                    case SAVE:
                        //保存
                        ProductionPlan productionPlan = new ProductionPlan();
                        ToolUtil.copyProperties(productionPlanParam, productionPlan);
                        productionPlanService.save(productionPlan);
                        break;
                    case UPDATE:
                        productionPlanService.update(productionPlanParam);
                        break;
                    case DELETE:
                        productionPlanService.delete(productionPlanParam);
                        break;
                }
                break;
            case WORK_ORDER:
                switch (microServiceEntity.getOperationType()) {
                    case ADD:
                        List<ProductionCard> cardList = productionCardService.addBatchCardByProductionPlan(microServiceEntity.getObject());
                        productionWorkOrderService.microServiceAdd(microServiceEntity.getObject());
                        break;

                }
                break;
            case PRODUCTION_PICKLISTS:
                switch (microServiceEntity.getOperationType()) {
                    case ADD:
                        productionPickListsService.addByProductionTask(microServiceEntity.getObject());
                        break;
                }
                break;
            case QUALITY_TASK:
                switch (microServiceEntity.getOperationType()) {
                    case ADD:
                        QualityTaskParam qualityTaskParam = JSON.parseObject(microServiceEntity.getObject().toString(), QualityTaskParam.class);
                        qualityTaskService.microAdd(qualityTaskParam);
                        break;
                }
                break;
            case INSTOCKORDER:
                switch (microServiceEntity.getOperationType()) {
                    case SAVE:
                        InstockOrderParam instockOrderParam = JSON.parseObject(microServiceEntity.getObject().toString(), InstockOrderParam.class);
                        instockOrderService.addRecord(instockOrderParam);
                        break;
                }
                break;
            case OUTSTOCKORDER:
                switch (microServiceEntity.getOperationType()) {
                    case SAVE:
                        OutstockOrderParam outstockOrderParam = JSON.parseObject(microServiceEntity.getObject().toString(), OutstockOrderParam.class);
                        outstockOrderService.addRecord(outstockOrderParam);
                        break;
                }
                break;
            case Announcements:
                switch (microServiceEntity.getOperationType()) {
                    case ORDER:
                        List<Long> longs = JSON.parseArray(microServiceEntity.getObject().toString(), Long.class);
                        announcementsService.toJson(longs);
                        break;
                }
            case MAINTENANCE:
                switch (microServiceEntity.getOperationType()) {
                    case SAVEDETAILS:
                        Maintenance maintenance = (Maintenance) microServiceEntity.getObject();
                        maintenanceService.saveDetails(maintenance);
                        break;
                }
            case DYNAMIC:
                switch (microServiceEntity.getOperationType()) {
                    case SAVE:
                        DynamicParam dynamic = JSON.parseObject(microServiceEntity.getObject().toString(), DynamicParam.class);
                        dynamicService.add(dynamic);
                        break;
                }
            case INSTOCK_VIEW_EXCEL:
                switch (microServiceEntity.getOperationType()) {
                    case PRINT:
                        DataStatisticsViewParam dataStatisticsViewParam = JSON.parseObject(microServiceEntity.getObject().toString(), DataStatisticsViewParam.class);
                        instockViewExcel.excel(dataStatisticsViewParam);
                        break;
                }
                break;
            case OUTSTOCK_VIEW_EXCEL:
                switch (microServiceEntity.getOperationType()) {
                    case PRINT:
                        DataStatisticsViewParam dataStatisticsViewParam = JSON.parseObject(microServiceEntity.getObject().toString(), DataStatisticsViewParam.class);
                        outStockViewExcel.excel(dataStatisticsViewParam);
                        break;
                }
                break;
            case INIT:
                switch (microServiceEntity.getOperationType()) {
                    case DEFAULT:
                        //初始化
                        initTenantService.initTenant((Long) microServiceEntity.getObject());
                        break;
                }
                default:
                    break;
        }
    }
}

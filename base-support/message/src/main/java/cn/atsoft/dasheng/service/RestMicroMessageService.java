package cn.atsoft.dasheng.service;

import org.springframework.stereotype.Service;

@Service
public class RestMicroMessageService {

//
//    @Autowired
//    private ProductionPlanService productionPlanService;
//
//    @Autowired
//    private InstockOrderService instockOrderService;
//
//    @Autowired
//    private OutstockOrderService outstockOrderService;
//
//    @Autowired
//    private ProductionWorkOrderService productionWorkOrderService;
//
//    @Autowired
//    private ProductionCardService productionCardService;
//
//    @Autowired
//    private ProductionPickListsService productionPickListsService;
//
//    @Autowired
//    private QualityTaskService qualityTaskService;
//
//    @Autowired
//    private AnnouncementsService announcementsService;
//
//    @Autowired
//    private MaintenanceService maintenanceService;
//
//    @Autowired
//    private DynamicService dynamicService;
//
//    @Autowired
//    private InstockViewExcel instockViewExcel;
//
//    @Autowired
//    private OutStockViewExcel outStockViewExcel;
//
//
//    public void microServiceDo(MicroServiceEntity microServiceEntity) throws IOException {
//        switch (microServiceEntity.getType()) {
//            case CONTRACT:
//
//                switch (microServiceEntity.getOperationType()) {
//                    case SAVE:
//                        //保存
//
//                        break;
//                    case ADD:
//                        ContractParam contractParam = JSON.parseObject(microServiceEntity.getObject().toString(), ContractParam.class);
//                        break;
//                }
//                break;
//
//            case PRODUCTION_PLAN:
//                ProductionPlanParam productionPlanParam = JSON.parseObject(microServiceEntity.getObject().toString(), ProductionPlanParam.class);
//                switch (microServiceEntity.getOperationType()) {
//                    case ADD:
//                        productionPlanService.add(productionPlanParam);
//                        break;
//                    case SAVE:
//                        //保存
//                        ProductionPlan productionPlan = new ProductionPlan();
//                        ToolUtil.copyProperties(productionPlanParam, productionPlan);
//                        productionPlanService.save(productionPlan);
//                        break;
//                    case UPDATE:
//                        productionPlanService.update(productionPlanParam);
//                        break;
//                    case DELETE:
//                        productionPlanService.delete(productionPlanParam);
//                        break;
//                }
//                break;
//            case WORK_ORDER:
//                switch (microServiceEntity.getOperationType()) {
//                    case ADD:
//                        List<ProductionCard> cardList = productionCardService.addBatchCardByProductionPlan(microServiceEntity.getObject());
//                        productionWorkOrderService.microServiceAdd(microServiceEntity.getObject());
//                        break;
//
//                }
//                break;
//            case PRODUCTION_PICKLISTS:
//                switch (microServiceEntity.getOperationType()) {
//                    case ADD:
//                        productionPickListsService.addByProductionTask(microServiceEntity.getObject());
//                        break;
//                }
//                break;
//            case QUALITY_TASK:
//                switch (microServiceEntity.getOperationType()) {
//                    case ADD:
//                        QualityTaskParam qualityTaskParam = JSON.parseObject(microServiceEntity.getObject().toString(), QualityTaskParam.class);
//                        qualityTaskService.microAdd(qualityTaskParam);
//                        break;
//                }
//                break;
//            case INSTOCKORDER:
//                switch (microServiceEntity.getOperationType()) {
//                    case SAVE:
//                        InstockOrderParam instockOrderParam = JSON.parseObject(microServiceEntity.getObject().toString(), InstockOrderParam.class);
//                        instockOrderService.addRecord(instockOrderParam);
//                        break;
//                }
//                break;
//            case OUTSTOCKORDER:
//                switch (microServiceEntity.getOperationType()) {
//                    case SAVE:
//                        OutstockOrderParam outstockOrderParam = JSON.parseObject(microServiceEntity.getObject().toString(), OutstockOrderParam.class);
//                        outstockOrderService.addRecord(outstockOrderParam);
//                        break;
//                }
//                break;
//            case Announcements:
//                switch (microServiceEntity.getOperationType()) {
//                    case ORDER:
//                        List<Long> longs = JSON.parseArray(microServiceEntity.getObject().toString(), Long.class);
//                        announcementsService.toJson(longs);
//                        break;
//                }
//            case MAINTENANCE:
//                switch (microServiceEntity.getOperationType()) {
//                    case SAVEDETAILS:
//                        Maintenance maintenance = (Maintenance) microServiceEntity.getObject();
//                        maintenanceService.saveDetails(maintenance);
//                        break;
//                }
//            case DYNAMIC:
//                switch (microServiceEntity.getOperationType()) {
//                    case SAVE:
//                        DynamicParam dynamic = JSON.parseObject(microServiceEntity.getObject().toString(), DynamicParam.class);
//                        dynamicService.add(dynamic);
//                        break;
//                }
//            case INSTOCK_VIEW_EXCEL:
//                switch (microServiceEntity.getOperationType()) {
//                    case PRINT:
//                        DataStatisticsViewParam dataStatisticsViewParam = JSON.parseObject(microServiceEntity.getObject().toString(), DataStatisticsViewParam.class);
//                        instockViewExcel.excel(dataStatisticsViewParam);
//                        break;
//                }
//                break;
//            case OUTSTOCK_VIEW_EXCEL:
//                switch (microServiceEntity.getOperationType()) {
//                    case PRINT:
//                        DataStatisticsViewParam dataStatisticsViewParam = JSON.parseObject(microServiceEntity.getObject().toString(), DataStatisticsViewParam.class);
//                        outStockViewExcel.excel(dataStatisticsViewParam);
//                        break;
//                }
//                break;
//            default:
//        }
//    }
}

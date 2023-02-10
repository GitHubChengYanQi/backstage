package cn.atsoft.dasheng.form.model.enums;

public enum ModelEnum {
    QUALITY("质检")
//            ("质检", "QUALITY", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.inQuality);
//        add(ProcessModuleEnum.outQuality);
//        add(ProcessModuleEnum.productionQuality);
//    }})
    ,

    INSTOCK("入库单")
//            ("入库单", "INSTOCK", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.productionInstock);
//        add(ProcessModuleEnum.purchaseInstock);
//        add(ProcessModuleEnum.createInstock);
//    }})
    ,

    SHIP("工艺路线")
//            ("工艺路线", "SHIP", new ArrayList<ProcessModuleEnum>() {{
//
//    }})
    ,
    PURCHASEASK("采购申请")
//            ("采购申请", "PURCHASEASK", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.purchaseAsk);
//    }})
    ,
    PROCUREMENTORDER("采购单")
//            ("采购单", "PROCUREMENTORDER", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.procurementOrder);
//    }})
    ,

    ERROR("异常")
//            ("异常", "ERROR", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.INSTOCKERROR);
//        add(ProcessModuleEnum.StocktakingError);
//    }})
    ,

    MAINTENANCE("养护申请")
//            ("养护申请" ,"MAINTENANCE",new ArrayList<ProcessModuleEnum>(){{
//        add(ProcessModuleEnum.reMaintenance);
//    }})
    ,
    OUTSTOCK("出库单")
//            ("出库单", "OUTSTOCK", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.productionOutStock);
//        add(ProcessModuleEnum.pickLists);
//    }})
    ,
    ALLOCATION("调拨")
//            ("调拨", "ALLOCATION", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.allocation);
//    }})
    ,
    Stocktaking("盘点")
//            ("盘点", "Stocktaking", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.Stocktaking);
//    }})
    ,
    ProductionTask("生产任务")
//            ("生产任务", "productionTask", new ArrayList<ProcessModuleEnum>() {{
//        add(ProcessModuleEnum.productionTask);
//    }})
    ;
    private String name;

    public String getName() {
        return name;
    }

    ModelEnum(String name) {
        this.name = name;
    }
}

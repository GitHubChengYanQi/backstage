package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProcessType {
    QUALITY("质检", "QUALITY", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.inQuality);
        add(ProcessModuleEnum.outQuality);
        add(ProcessModuleEnum.productionQuality);
    }}),

    INSTOCK("入库单", "INSTOCK", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.productionInstock);
        add(ProcessModuleEnum.purchaseInstock);
        add(ProcessModuleEnum.createInstock);
    }}),

    SHIP("工艺路线", "SHIP", new ArrayList<ProcessModuleEnum>() {{

    }}),
    PURCHASEASK("采购申请", "PURCHASEASK", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.purchaseAsk);
    }}),
    PROCUREMENTORDER("采购单", "PROCUREMENTORDER", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.procurementOrder);
    }}),
    ERROR("异常", "ERROR", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.INSTOCKERROR);
        add(ProcessModuleEnum.StocktakingError);
    }}),

    MAINTENANCE("养护申请" ,"MAINTENANCE",new ArrayList<ProcessModuleEnum>(){{
        add(ProcessModuleEnum.reMaintenance);
        }}),
    OUTSTOCK("出库单", "OUTSTOCK", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.productionOutStock);
        add(ProcessModuleEnum.pickLists);
    }}),
    ALLOCATION("调拨", "ALLOCATION", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.allocation);
    }}),
    Stocktaking("盘点", "Stocktaking", new ArrayList<ProcessModuleEnum>() {{
        add(ProcessModuleEnum.Stocktaking);
    }});




    //
//    @EnumValue
//    private final Map<String, String> detail;
    @EnumValue
    private List<Map<String, Object>> list;
    @EnumValue
    private List<ProcessModuleEnum> moduleEnums;


    @EnumValue
    private String name;
    @EnumValue
    private String type;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }


    public List<Map<String, Object>> getList() {
        return list;
    }

    public List<ProcessModuleEnum> getModuleEnums() {
        return moduleEnums;
    }


    ProcessType(String name, String type, List<ProcessModuleEnum> moduleEnums) {
        this.name = name;
        this.type = type;
        this.moduleEnums = moduleEnums;

    }

//    ProcessType(String name, String type, List<Map<String,Object>> list) {
//        this.name = name;
//        this.type = type;
//        this.list = list;
//    }


    @Override
    public String toString() {
        return "ProcessType{" +
                ", name=" + name +
                ", list=" + list +
                '}';
    }


    public static List<Map<String, Object>> enumList() {
        List<Map<String, Object>> enumList = new ArrayList<>();
        for (ProcessType value : ProcessType.values()) {
            Map<String, Object> enumDetail = new HashMap<>();
            List<Map<String, String>> detail = new ArrayList<>();
            for (ProcessModuleEnum anEnum : value.getModuleEnums()) {
                Map<String, String> map = new HashMap<>();
                map.put("module", anEnum.name());
                map.put("moduleName", anEnum.getModuleName());
                detail.add(map);
            }
            String type = value.getType();
            String name = value.getName();
            enumDetail.put("details", detail);
            enumDetail.put("name", name);
            enumDetail.put("type", type);
            enumList.add(enumDetail);
        }
        return enumList;
    }

    public static String getNameByEnum(String param){
        for (ProcessType value : ProcessType.values()) {
            if (value.name().equals(param)){
                return value.getName();
            }
        }
        return null;
    }
}


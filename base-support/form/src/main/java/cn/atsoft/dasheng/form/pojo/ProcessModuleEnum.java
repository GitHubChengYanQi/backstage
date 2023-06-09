package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProcessModuleEnum {

    inQuality("入场检"),
    outQuality("出厂检"),
    productionQuality("生产检"),
    productionInstock("生产入库"),
    purchaseInstock("采购入库"),
    purchaseAsk("采购申请"),
    purchasePlan("采购计划"),
    procurementOrder("采购单"),
    inQuiry(""),
    instockError(""),
    createInstock("直接入库"),
    INSTOCK(""),
    quality_task(""),
    verifyError("核实异常"),
    productionOutStock("生产出库"),
    Stocktaking("普通盘点"),
    INSTOCKERROR("入库异常"),
    StocktakingError("盘点异常"),
    pickLists("出库单申请"),
    allocation("调拨申请"),
    reMaintenance("复检复调"),
    productionTask("生产任务"),
    route("工艺路线"),
    ships("工艺路线");

    @EnumValue
    private String moduleName;

    @Override
    public String toString() {
        return "ProcessModuleEnum{" +
                "name='" + moduleName + '\'' +
                '}';
    }

    public String getModuleName() {
        return moduleName;
    }

    ProcessModuleEnum(String moduleName) {
        this.moduleName = moduleName;
    }

    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (ProcessModuleEnum value : ProcessModuleEnum.values()) {
            Map<String, String> detail = new HashMap<>();
            detail.put("module", value.getModuleName());
            detail.put("moduleName", value.name());
            enumList.add(detail);
        }
        return enumList;
    }
    public static String getModelNameByEnum(String param){
        for (ProcessModuleEnum value : ProcessModuleEnum.values()) {
            if (value.name().equals(param)){
                return value.moduleName;
            }
        }
        return null;
    }
}

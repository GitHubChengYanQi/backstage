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
    createInstock(""),
    INSTOCK(""),
    quality_task(""),
    verifyError("核实异常");


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
            Map<String,String> detail = new HashMap<>();
            detail.put("module",value.getModuleName());
            detail.put("moduleName", value.name());
            enumList.add(detail);
        }
        return enumList;
    }
}

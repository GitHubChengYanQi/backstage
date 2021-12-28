package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ProcessModuleEnum {

    入厂检("inQuality", "入厂检"),
    采购申请("purchaseAsk", "采购申请"),
    采购计划("purchasePlan", "采购计划");

    @EnumValue
    private final String type;
    @EnumValue
    private final String name;

    ProcessModuleEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return "ProcessModuleEnum{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

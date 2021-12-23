package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ProcessEnum {
    purchasePlan("purchasePlan","采购计划"),
    purchaseAsk("purchaseAsk","采购申请单"),
    inQuality("inQuality","入场检"),
    outQuality("outQuality","出场检");

    @EnumValue
    private final String type;
    @EnumValue
    private final String name;

    ProcessEnum(String type, String name) {
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
        return "ProcessEnum{" +
                "type='" + type + '\'' +
                ", value='" + name + '\'' +
                '}';
    }
}

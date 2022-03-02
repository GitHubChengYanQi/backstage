package cn.atsoft.dasheng.purchase.model.params;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SourceEnum {
    ORDER("order"),
    PURCHASEASK("purchase"),
    CONTRACT("contract"),
    WORKORDER("workOrder"),
    PRODUCTIONPLAN("productionPlan");




    @EnumValue
    private final String source;

    SourceEnum(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SourceEnum{" +
                "source='" + source + '\'' +
                '}';
    }

    public String getSource() {
        return source;
    }
}

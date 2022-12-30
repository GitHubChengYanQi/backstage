package cn.atsoft.dasheng.erp.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum StockLogDetailSourceEnum implements IEnum<String> {
    outstock("出库","outstock"),
    instock("入库","instock"),
    inventory("盘点","inventory");

    private final String name;

    private final String value;

    StockLogDetailSourceEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}

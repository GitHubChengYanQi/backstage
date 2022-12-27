package cn.atsoft.dasheng.erp.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum StockLogTypeEnum implements IEnum<String> {
    increase("增加","increase"),
    dwindle("减少","dwindle");

    private final String name;

    private final String value;

    StockLogTypeEnum(String name, String value) {
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

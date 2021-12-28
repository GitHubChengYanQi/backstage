package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ProcessEnum {
    质检("quality", "质检"),
    询价("enquiry", "询价"),
    采购("purchase", "采购");


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
                ", name='" + name + '\'' +
                '}';
    }
}

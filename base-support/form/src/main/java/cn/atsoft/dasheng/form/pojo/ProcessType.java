package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ProcessType {


    quality("质检"),
    purchase("采购"),
    enquiry("询价");

    ProcessType(String type) {
        this.type = type;
    }

    @EnumValue
    private final String type;

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}

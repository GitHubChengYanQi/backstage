package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum printTemplateEnum {
    test("测试");



    printTemplateEnum(String name){
        this.name = name;
    }

    @EnumValue
    private final String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "printTemplateEnum{" +
                "name='" + name + '\'' +
                '}';
    }
}

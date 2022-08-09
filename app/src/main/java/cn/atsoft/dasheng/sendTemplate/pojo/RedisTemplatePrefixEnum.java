package cn.atsoft.dasheng.sendTemplate.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum RedisTemplatePrefixEnum {
    LLM("领料码","LLM_"),
    LLJCM("领料检查码","LLJCM_");

    @EnumValue
    private String enumName;



    public String getValue() {
        return value;
    }

    @EnumValue
    private String value;



    public String getEnumName() {
        return enumName;
    }

    RedisTemplatePrefixEnum(String enumName) {
        this.enumName = enumName;
    }

    RedisTemplatePrefixEnum(String enumName, String value) {
        this.enumName = enumName;
        this.value = value;
    }

    @Override
    public String toString() {
        return "RedisTemplatePrefixEnum{" +
                "enumName='" + enumName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}

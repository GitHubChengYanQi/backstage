package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;



public enum StepsType {

    /**
     * 定义类型
     */

    START("0"),
    AUDIT("1"),
    SEND("2"),
    BRANCH("3"),
    ROUTE("4");



    StepsType(String type) {

        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static StepsType getByType(String type){
        for (StepsType value : StepsType.values()) {
            if(type.equals(value.getType())){
                return value;
            }
        }
        return null;
    }

    @EnumValue
    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
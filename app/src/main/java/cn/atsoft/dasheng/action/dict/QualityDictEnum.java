package cn.atsoft.dasheng.action.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum QualityDictEnum {

    coding("coding", "编码")
    , type("type", "类型")
    , status("status", "申请状态")
    , remark("remark", "备注")
    , number("number", "总数量")
    , userId("userId", "负责人")
    , time("time", "时间")
    ;

    private String name;
    private String filedName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    QualityDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (QualityDictEnum value : QualityDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

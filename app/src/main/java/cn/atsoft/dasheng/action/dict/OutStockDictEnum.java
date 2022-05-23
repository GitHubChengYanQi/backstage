package cn.atsoft.dasheng.action.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OutStockDictEnum {

    coding("coding", "编码")
    , type("type", "类型")
    , state("status", "出库状态")
    , note("note", "备注")
    , number("number", "总数量")
    , userId("userId", "经手人")
    , time("time", "计划出库时间")
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

    OutStockDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (OutStockDictEnum value : OutStockDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

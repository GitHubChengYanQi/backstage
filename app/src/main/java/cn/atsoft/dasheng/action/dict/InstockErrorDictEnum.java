package cn.atsoft.dasheng.action.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InstockErrorDictEnum {

    orderResult("orderResult", "入库单")
    , remark("remark", "备注")
    , anomalyId("anomalyId", "异常id")
    , theme("theme", "主题")
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

    InstockErrorDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (InstockErrorDictEnum value : InstockErrorDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

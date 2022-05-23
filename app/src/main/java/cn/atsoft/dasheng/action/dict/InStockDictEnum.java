package cn.atsoft.dasheng.action.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InStockDictEnum {

    storeHouseId("storeHouseId", "仓库id")
    , userId("userId", "负责人")
    , stockUserId("stockUserId", "库管人员负责人")
    , urgent("urgent", "是否加急")
    , enclosure("enclosure", "附件")
    , instockTime("instockTime", "入库时间")
    , state("state", "入库状态")
    , storehousePositionsId("storehousePositionsId", "库位id")
    , coding("coding", "编码");

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

    InStockDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (InStockDictEnum value : InStockDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

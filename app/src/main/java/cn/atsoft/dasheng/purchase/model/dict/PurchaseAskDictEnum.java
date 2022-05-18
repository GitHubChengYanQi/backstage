package cn.atsoft.dasheng.purchase.model.dict;

import cn.atsoft.dasheng.form.pojo.MoneyTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PurchaseAskDictEnum {

    purchaseAskId("purchaseAskId", "采购申请id")
    , coding("coding", "编号")
    , type("type", "采购类型")
    , note("note", "备注")
    , status("status", "申请状态")
    , money("money", "总金额")
    , number("number", "总数量")
    , typeNumber("typeNumber", "种类数量")
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

    PurchaseAskDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (PurchaseAskDictEnum value : PurchaseAskDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

package cn.atsoft.dasheng.action.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderDictEnum {

    orderId("orderId", "订单编号")
    , contractId("contractId", "合同id")
    , price("price", "订单金额")
    , buyerId("buyerId", "买方id")
    , sellerId("sellerId", "卖方id")
    , userId("userId", "负责人")
    , remark("remark", "备注")
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

    OrderDictEnum(String name, String filedName) {
        this.name = name;
        this.filedName = filedName;
    }


    public static List<Map<String, String>> enumList() {
        List<Map<String, String>> enumList = new ArrayList<>();
        for (OrderDictEnum value : OrderDictEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label",value.name);
            map.put("value",value.filedName);
            enumList.add(map);
        }
        return enumList;
    }
}

package cn.atsoft.dasheng.erp.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorehousePositionsSelectWrapper extends BaseControllerWrapper {

    public StorehousePositionsSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String value = Convert.toStr(map.get("storehouse_positions_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
    public static List<String> fetchParentKey(List<Map<String, Object>> list, String key) {

        if(key.equals("0")){
            List<String> tmp = new ArrayList<>();
            tmp.add("0");
            return tmp;
        }
        List<String> result = new ArrayList<>();
        List<String> parentResult = new ArrayList<>();

        for (Map<String, Object> item : list) {
            String value = Convert.toStr(item.get("storehouse_positions_id"));
            String parentValue = Convert.toStr(item.get("pid"));
            if (key.equals(value)) {
                result.add(value);
                parentResult = fetchParentKey(list, parentValue);
                parentResult.addAll(result);
            }
        }
        return parentResult;
    }
}
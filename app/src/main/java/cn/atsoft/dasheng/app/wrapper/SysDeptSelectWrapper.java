package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SysDeptSelectWrapper extends BaseControllerWrapper {

    public SysDeptSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("full_name"));
        String value = Convert.toStr(map.get("dept_id"));
        map.clear();
        map.put("label", label);
        map.put("value", value);
    }

    public static List<String> fetchParentKey(List<Map<String, Object>> list, String key) {
        
        List<String> result = new ArrayList<>();
        List<String> parentResult = new ArrayList<>();

        for (Map<String, Object> item : list) {
            if (key.equals(item.get("pid"))) {
                result.add(Convert.toStr(item.get("pid")));
                parentResult = fetchParentKey(list, Convert.toStr(item.get("pid")));
                parentResult.addAll(result);
            }
        }
        return parentResult;
    }
}
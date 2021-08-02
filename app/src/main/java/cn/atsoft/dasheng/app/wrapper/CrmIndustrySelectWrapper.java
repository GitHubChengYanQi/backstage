package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrmIndustrySelectWrapper extends BaseControllerWrapper {

    public CrmIndustrySelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("industry_name"));
        String value = Convert.toStr(map.get("industry_id"));
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
            String value = Convert.toStr(item.get("industry_id"));
            String parentValue = Convert.toStr(item.get("parent_id"));
            if (key.equals(value)) {
                result.add(value);
                parentResult = fetchParentKey(list, parentValue);
                parentResult.addAll(result);
            }
        }
        return parentResult;
    }
}
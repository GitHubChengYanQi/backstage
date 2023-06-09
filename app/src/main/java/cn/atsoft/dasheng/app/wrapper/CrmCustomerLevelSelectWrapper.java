package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class CrmCustomerLevelSelectWrapper extends BaseControllerWrapper {

    public CrmCustomerLevelSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("level"));
        String rank = Convert.toStr(map.get("rank"));
        String value = Convert.toStr(map.get("customer_level_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
        map.put("rank",rank);
    }
}
package cn.atsoft.dasheng.erp.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityPlanSelectWrapper extends BaseControllerWrapper {

    public QualityPlanSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("plan_name"));
        String value = Convert.toStr(map.get("quality_plan_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
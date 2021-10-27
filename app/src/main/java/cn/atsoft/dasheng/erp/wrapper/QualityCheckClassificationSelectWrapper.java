package cn.atsoft.dasheng.erp.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityCheckClassificationSelectWrapper extends BaseControllerWrapper {

    public QualityCheckClassificationSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String value = Convert.toStr(map.get("quality_check_classification_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
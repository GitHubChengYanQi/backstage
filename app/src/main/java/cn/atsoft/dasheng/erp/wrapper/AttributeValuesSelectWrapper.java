package cn.atsoft.dasheng.erp.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeValuesSelectWrapper extends BaseControllerWrapper {

    public AttributeValuesSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("attribute_values_id"));
        String value = Convert.toStr(map.get("values"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
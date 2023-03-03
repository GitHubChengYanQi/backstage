package cn.atsoft.dasheng.goods.classz.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class RestAttributeValuesSelectWrapper extends BaseControllerWrapper {

    public RestAttributeValuesSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("attribute_values_id"));
        String value = Convert.toStr(map.get("attributeValues"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
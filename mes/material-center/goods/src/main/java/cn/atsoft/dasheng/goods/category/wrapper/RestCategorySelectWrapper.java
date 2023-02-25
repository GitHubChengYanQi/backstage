package cn.atsoft.dasheng.goods.category.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class RestCategorySelectWrapper extends BaseControllerWrapper {

    public RestCategorySelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String value = Convert.toStr(map.get("spu_classification_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
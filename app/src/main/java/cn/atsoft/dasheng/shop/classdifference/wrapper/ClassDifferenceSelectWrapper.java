package cn.atsoft.dasheng.shop.classdifference.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class ClassDifferenceSelectWrapper extends BaseControllerWrapper {

    public ClassDifferenceSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("title"));
        String value = Convert.toStr(map.get("class_difference_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
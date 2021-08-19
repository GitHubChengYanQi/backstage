package cn.atsoft.dasheng.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class BannerSelectWrapper extends BaseControllerWrapper {

    public BannerSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("difference"));
        String value = Convert.toStr(map.get("classification_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
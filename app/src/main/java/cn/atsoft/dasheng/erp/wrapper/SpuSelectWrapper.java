package cn.atsoft.dasheng.erp.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpuSelectWrapper extends BaseControllerWrapper {

    public SpuSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String model = Convert.toStr(map.get("model"));
        String value = Convert.toStr(map.get("spu_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
        map.put("model",model);
    }
}
package cn.atsoft.dasheng.sys.modular.rest.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class RestUserSelectWrapper extends BaseControllerWrapper {

    public RestUserSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("account"));
        String value = Convert.toStr(map.get("user_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
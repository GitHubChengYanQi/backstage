package cn.atsoft.dasheng.api.uc.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenUserInfoSelectWrapper extends BaseControllerWrapper {

    public OpenUserInfoSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("username"));
        String value = Convert.toStr(map.get("member_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
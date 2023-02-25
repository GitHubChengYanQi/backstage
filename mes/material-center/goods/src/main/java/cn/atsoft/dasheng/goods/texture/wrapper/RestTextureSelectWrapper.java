package cn.atsoft.dasheng.goods.texture.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class RestTextureSelectWrapper extends BaseControllerWrapper {

    public RestTextureSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String value = Convert.toStr(map.get("material_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
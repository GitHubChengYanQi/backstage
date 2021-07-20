package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdressSelectWrapper extends BaseControllerWrapper {

    public AdressSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
      String label = Convert.toStr(map.get("adress_id"));
      String value = Convert.toStr(map.get("adress_id"));
      map.clear();
      map.put("label",label);
      map.put("value",value);
    }
}
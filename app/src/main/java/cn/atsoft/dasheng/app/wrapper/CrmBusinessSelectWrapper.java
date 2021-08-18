package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Map;

public class CrmBusinessSelectWrapper extends BaseControllerWrapper {

    public CrmBusinessSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
      String label = Convert.toStr(map.get("location"));
      String value = Convert.toStr(map.get("location"));
      map.clear();
      map.put("label",label);
      map.put("value",value);
    }
}

package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactsSelectWrapper1 extends BaseControllerWrapper {

  public ContactsSelectWrapper1(List<Map<String, Object>> multi1) {
    super(multi1);
  }
  @Override
  protected void wrapTheMap(Map<String, Object> map) {
    String label = Convert.toStr(map.get("phone"));
    String value = Convert.toStr(map.get("contacts_id"));
    map.clear();
    map.put("label",label);
    map.put("value",value);
  }
}


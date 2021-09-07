package cn.atsoft.dasheng.crm.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompanyRoleSelectWrapper extends BaseControllerWrapper {

    public CompanyRoleSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("position"));
        String value = Convert.toStr(map.get("company_role_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }
}
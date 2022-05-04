package cn.atsoft.dasheng.form.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;
import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentsStatusSelectWrapper extends BaseControllerWrapper {

    public DocumentsStatusSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String label = Convert.toStr(map.get("name"));
        String value = Convert.toStr(map.get("documents_status_id"));
        map.clear();
        map.put("label",label);
        map.put("value",value);
    }


}
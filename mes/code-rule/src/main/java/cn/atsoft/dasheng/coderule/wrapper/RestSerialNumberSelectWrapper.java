package cn.atsoft.dasheng.coderule.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class RestSerialNumberSelectWrapper extends BaseControllerWrapper {

    public RestSerialNumberSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
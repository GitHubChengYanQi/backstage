package cn.atsoft.dasheng.app.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class MessageSelectWrapper extends BaseControllerWrapper {

    public MessageSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
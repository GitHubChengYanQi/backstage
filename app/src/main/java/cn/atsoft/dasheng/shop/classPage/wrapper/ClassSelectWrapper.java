package cn.atsoft.dasheng.shop.classPage.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class ClassSelectWrapper extends BaseControllerWrapper {

    public ClassSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
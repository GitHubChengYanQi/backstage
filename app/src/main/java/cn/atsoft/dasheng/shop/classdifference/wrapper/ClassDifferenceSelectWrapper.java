package cn.atsoft.dasheng.shop.classdifference.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class ClassDifferenceSelectWrapper extends BaseControllerWrapper {

    public ClassDifferenceSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
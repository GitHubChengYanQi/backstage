package cn.atsoft.dasheng.shop.classDifferenceDetail.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class ClassDifferenceDetailsSelectWrapper extends BaseControllerWrapper {

    public ClassDifferenceDetailsSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
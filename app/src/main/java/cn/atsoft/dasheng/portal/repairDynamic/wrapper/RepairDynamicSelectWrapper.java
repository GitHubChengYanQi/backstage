package cn.atsoft.dasheng.portal.repairDynamic.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class RepairDynamicSelectWrapper extends BaseControllerWrapper {

    public RepairDynamicSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
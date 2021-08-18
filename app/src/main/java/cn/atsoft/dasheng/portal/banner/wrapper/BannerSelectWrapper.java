package cn.atsoft.dasheng.portal.banner.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class BannerSelectWrapper extends BaseControllerWrapper {

    public BannerSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
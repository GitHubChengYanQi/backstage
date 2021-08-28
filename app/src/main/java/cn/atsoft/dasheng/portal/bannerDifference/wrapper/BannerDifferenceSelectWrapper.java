package cn.atsoft.dasheng.portal.bannerDifference.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class BannerDifferenceSelectWrapper extends BaseControllerWrapper {

    public BannerDifferenceSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
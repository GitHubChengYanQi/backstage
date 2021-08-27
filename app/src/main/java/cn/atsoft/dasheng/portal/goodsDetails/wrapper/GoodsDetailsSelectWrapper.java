package cn.atsoft.dasheng.portal.goodsDetails.wrapper;

import cn.atsoft.dasheng.core.base.warpper.BaseControllerWrapper;

import java.util.List;
import java.util.Map;

public class GoodsDetailsSelectWrapper extends BaseControllerWrapper {

    public GoodsDetailsSelectWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
    }
}
package cn.atsoft.dasheng.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;

import java.util.Map;

public interface IErpBase {
    PageInfo getOrderList(Map<String, Object> param);
    PageInfo getOrderDetailList(Map<String, Object> param);
}

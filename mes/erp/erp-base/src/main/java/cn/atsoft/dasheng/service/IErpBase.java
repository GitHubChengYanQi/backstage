package cn.atsoft.dasheng.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.entity.RestOrder;
import cn.atsoft.dasheng.entity.RestOrderDetail;
import cn.atsoft.dasheng.model.result.RestOrderDetailResult;

import java.util.List;
import java.util.Map;

public interface IErpBase {
    PageInfo getOrderList(Map<String, Object> param);
    List<RestOrderDetailResult> getOrderDetailList(Map<String, Object> param);
    List<RestOrderDetail> getDetailListByOrderId(Long orderId);
    List<RestOrderDetail> getDetailListByOrderDetailIds(List<Long> detailIds);
    void updateDetailList(List<RestOrderDetail> dataList);
    RestOrder getOrderById(Long orderId);


}

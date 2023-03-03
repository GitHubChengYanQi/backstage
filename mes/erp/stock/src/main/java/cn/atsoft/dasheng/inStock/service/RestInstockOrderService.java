package cn.atsoft.dasheng.inStock.service;

import cn.atsoft.dasheng.inStock.entity.RestInstockOrder;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderParam;
import cn.atsoft.dasheng.inStock.model.result.OrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface RestInstockOrderService extends IService<RestInstockOrder> {
    public List<OrderResult> showOrderList();

    Long autoInStock(RestInstockOrderParam param);
}

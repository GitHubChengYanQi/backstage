package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Order;
import cn.atsoft.dasheng.app.model.params.OrderParam;
import cn.atsoft.dasheng.app.model.result.OrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
public interface OrderService extends IService<Order> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-20
     */
    Long add(OrderParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-20
     */
    void delete(OrderParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-20
     */
    void update(OrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
    OrderResult findBySpec(OrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
    List<OrderResult> findListBySpec(OrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
     PageInfo<OrderResult> findPageBySpec(OrderParam param);

}

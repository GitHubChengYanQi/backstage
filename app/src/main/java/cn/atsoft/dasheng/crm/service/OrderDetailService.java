package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    void add(OrderDetailParam param);

    void addList(Long orderId, Long customerId, List<OrderDetailParam> params);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(OrderDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(OrderDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    OrderDetailResult findBySpec(OrderDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<OrderDetailResult> findListBySpec(OrderDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PageInfo<OrderDetailResult> findPageBySpec(OrderDetailParam param);

    List<OrderDetailResult> getDetails(Long orderId);

    void format(List<OrderDetailResult> param);

    List<OrderDetailResult> getOrderDettailProductionIsNull(OrderDetailParam paramCondition);
}

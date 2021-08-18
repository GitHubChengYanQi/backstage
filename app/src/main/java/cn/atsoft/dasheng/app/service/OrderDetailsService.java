package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OrderDetails;
import cn.atsoft.dasheng.app.model.params.OrderDetailsParam;
import cn.atsoft.dasheng.app.model.result.OrderDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface OrderDetailsService extends IService<OrderDetails> {

    /**
     * 新增
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void add(OrderDetailsParam param);

    /**
     * 删除
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void delete(OrderDetailsParam param);

    /**
     * 更新
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    void update(OrderDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    OrderDetailsResult findBySpec(OrderDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<OrderDetailsResult> findListBySpec(OrderDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author siqiang
     * @Date 2021-08-18
     */
     PageInfo<OrderDetailsResult> findPageBySpec(OrderDetailsParam param);

}

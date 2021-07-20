package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OrderBranch;
import cn.atsoft.dasheng.app.model.params.OrderBranchParam;
import cn.atsoft.dasheng.app.model.result.OrderBranchResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单分表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
public interface OrderBranchService extends IService<OrderBranch> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-20
     */
    void add(OrderBranchParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-20
     */
    void delete(OrderBranchParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-20
     */
    void update(OrderBranchParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
    OrderBranchResult findBySpec(OrderBranchParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
    List<OrderBranchResult> findListBySpec(OrderBranchParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-20
     */
     PageInfo<OrderBranchResult> findPageBySpec(OrderBranchParam param);

}

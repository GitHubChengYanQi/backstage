package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface OrderService extends IService<Order> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    Order add(OrderParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(OrderParam param);

    void updateContract(Long orderId, ContractParam contractParam);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(OrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    OrderResult findBySpec(OrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<OrderResult> findListBySpec(OrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PageInfo<OrderResult> findPageBySpec(OrderParam param);

    Map<String, Object> mapFormat(Long contractId);

    OrderResult getDetail(Long id);

    List<OrderResult> pendingProductionPlanByContracts(OrderParam orderParam);

    Set<ContractDetailSetRequest> pendingProductionPlan(OrderParam orderParam);

    void format(List<OrderResult> data);

    void checkStatus(Long orderId);
}

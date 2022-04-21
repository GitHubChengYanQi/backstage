package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 合同表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
public interface ContractService extends IService<Contract> {

    /**
     * 新增
     *
     * @return
     * @author
     * @Date 2021-07-21
     */
    Contract add(ContractParam param);

    /**
     * 删除
     *
     * @return
     * @author
     * @Date 2021-07-21
     */
    Contract delete(ContractParam param);

    /**
     * 更新
     *
     * @return
     * @author
     * @Date 2021-07-21
     */
    Contract update(ContractParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    ContractResult findBySpec(ContractParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    List<ContractResult> findListBySpec(ContractParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-21
     */
    PageInfo<ContractResult> findPageBySpec(ContractParam param, DataScope dataScope);

    Set<ContractDetailSetRequest> pendingProductionPlan();

    void batchDelete(List<Long> id);

    ContractResult detail(Long id);


    ContractResult addResult(ContractParam param);

    Contract orderAddContract(Long orderId, ContractParam param, OrderParam orderPara, String orderType);

    List<OrderDetailResult> skuReplaceList(Long orderId);

    List<PaymentDetailResult> paymentDetailsReplaceList(Long orderId);

    String skuReplace(String content, Long orderId);
}

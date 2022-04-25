package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.SupplyParam;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.aspectj.apache.bcel.generic.LineNumberGen;

import java.util.List;

/**
 * <p>
 * 供应商供应物料 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
public interface SupplyService extends IService<Supply> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-20
     */
    void add(SupplyParam param);



    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-20
     */
    void delete(SupplyParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-20
     */
    void update(SupplyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    SupplyResult findBySpec(SupplyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    List<SupplyResult> findListBySpec(SupplyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-20
     */
    PageInfo<SupplyResult> findPageBySpec(SupplyParam param);


    void addList(List<SupplyParam> supplyParams, Long customerId);

    void customerAdd(List<SupplyParam> supplyParams, Long customerId);

    List<SupplyResult> detail(Long customerId);

    List<SupplyResult> getListByCustomerId(Long customerId);

    List<CustomerResult> getSupplyByLevel(Long levelId, List<Long> skuIds);

    /**
     * 通过物料查询供应商
     *
     * @param skuIds
     * @return
     */
    List<CustomerResult> getSupplyBySku(List<Long> skuIds, Long supplierLevel);

    List<SupplyResult> getSupplyBySupplierId (Long supplierId);

    List<SupplyResult> getSupplyByCustomerIds(List<Long> customerIds);

    void OrdersBackFill(Long customerId, List<OrderDetailParam> params);
}

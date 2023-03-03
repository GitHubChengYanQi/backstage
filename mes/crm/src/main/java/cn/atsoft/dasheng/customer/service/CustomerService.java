package cn.atsoft.dasheng.customer.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.customer.entity.Customer;
import cn.atsoft.dasheng.customer.model.params.CustomerParam;
import cn.atsoft.dasheng.customer.model.result.CustomerResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户管理表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
public interface CustomerService extends IService<Customer> {

//    /**
//     * 新增
//     *
//     * @return
//     * @author
//     * @Date 2021-07-23
//     */
//    Customer add(CustomerParam param);
//
//
//    PageInfo pureList(CustomerParam param);
//
//    Long addCustomer(CustomerParam param);
//
//    /**
//     * 删除
//     *
//     * @return
//     * @author
//     * @Date 2021-07-23
//     */
//    Customer delete(CustomerParam param);
//
//    /**
//     * 更新
//     *
//     * @return
//     * @author
//     * @Date 2021-07-23
//     */
//    Customer update(CustomerParam param);
//
//    /**
//     * 更新负责人
//     *
//     * @param param
//     * @return
//     */
//
//    Customer updateChargePerson(CustomerParam param);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author
//     * @Date 2021-07-23
//     */
//    CustomerResult findBySpec(CustomerParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author
//     * @Date 2021-07-23
//     */
//    List<CustomerResult> findListBySpec(CustomerParam param);
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author
//     * @Date 2021-07-23
//     */
//    PageInfo findPageBySpec(DataScope dataScope, CustomerParam param);
//
//    /**
//     * 批量删除
//     *
//     * @param
//     */
//    void batchDelete(List<Long> customerId);
//
//    void updateStatus(CustomerParam customerParam);
//
//
//    CustomerResult detail(Long id);
//
//    List<CustomerResult> getResults(List<Long> ids);
//
//    List<CustomerResult> getSuppliers(Long levelId);

}

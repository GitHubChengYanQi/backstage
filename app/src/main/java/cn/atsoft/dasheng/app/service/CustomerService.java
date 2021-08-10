package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.result.CustomerIdRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
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

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-23
     */
    Long add(CustomerParam param);

    Long addCustomer(CustomerParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-23
     */
    void delete(CustomerParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-23
     */
    void update(CustomerParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-23
     */
    CustomerResult findBySpec(CustomerParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-23
     */
    List<CustomerResult> findListBySpec(CustomerParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-23
     */
    PageInfo<CustomerResult> findPageBySpec(CustomerParam param);

    /**
     * 批量删除
     * @param
     */
    void batchDelete( List<Long> customerId);

    void updateStatus (CustomerParam customerParam);
}

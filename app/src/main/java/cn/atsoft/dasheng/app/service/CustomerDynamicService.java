package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CustomerDynamic;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.model.result.CustomerDynamicResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户动态表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
public interface CustomerDynamicService extends IService<CustomerDynamic> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void add(CustomerDynamicParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void delete(CustomerDynamicParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void update(CustomerDynamicParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
    CustomerDynamicResult findBySpec(CustomerDynamicParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<CustomerDynamicResult> findListBySpec(CustomerDynamicParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
     PageInfo findPageBySpec(CustomerDynamicParam param, DataScope dataScope );

}

package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessDynamic;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.result.BusinessDynamicResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机动态表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
public interface BusinessDynamicService extends IService<BusinessDynamic> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void add(BusinessDynamicParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void delete(BusinessDynamicParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-10
     */
    void update(BusinessDynamicParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
    BusinessDynamicResult findBySpec(BusinessDynamicParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<BusinessDynamicResult> findListBySpec(BusinessDynamicParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-10
     */
     PageInfo findPageBySpec(BusinessDynamicParam param, DataScope dataScope );

}

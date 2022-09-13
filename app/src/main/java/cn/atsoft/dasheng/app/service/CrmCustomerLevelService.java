package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.model.params.CrmCustomerLevelParam;
import cn.atsoft.dasheng.app.model.result.CrmCustomerLevelResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户级别表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-30
 */
public interface CrmCustomerLevelService extends IService<CrmCustomerLevel> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-30
     */
    void add(CrmCustomerLevelParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-30
     */
    void delete(CrmCustomerLevelParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-30
     */
    void update(CrmCustomerLevelParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-30
     */
    CrmCustomerLevelResult findBySpec(CrmCustomerLevelParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-30
     */
    List<CrmCustomerLevelResult> findListBySpec(CrmCustomerLevelParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-30
     */
    PageInfo findPageBySpec(CrmCustomerLevelParam param, DataScope dataScope );

    void batchDelete(List<Long> ids);

}

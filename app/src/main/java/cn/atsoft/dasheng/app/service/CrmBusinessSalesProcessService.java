package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 销售流程 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessSalesProcessService extends IService<CrmBusinessSalesProcess> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-04
     */
    void add(CrmBusinessSalesProcessParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-04
     */
    void delete(CrmBusinessSalesProcessParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-04
     */
    void update(CrmBusinessSalesProcessParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    CrmBusinessSalesProcessResult findBySpec(CrmBusinessSalesProcessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessSalesProcessResult> findListBySpec(CrmBusinessSalesProcessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
     PageInfo findPageBySpec(CrmBusinessSalesProcessParam param, DataScope dataScope );

}

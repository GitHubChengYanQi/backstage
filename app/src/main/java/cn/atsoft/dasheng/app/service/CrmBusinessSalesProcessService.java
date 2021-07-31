package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 销售流程 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
public interface CrmBusinessSalesProcessService extends IService<CrmBusinessSalesProcess> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-31
     */
    void add(CrmBusinessSalesProcessParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-31
     */
    void delete(CrmBusinessSalesProcessParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-31
     */
    void update(CrmBusinessSalesProcessParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-31
     */
    CrmBusinessSalesProcessResult findBySpec(CrmBusinessSalesProcessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-31
     */
    List<CrmBusinessSalesProcessResult> findListBySpec(CrmBusinessSalesProcessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-31
     */
     PageInfo<CrmBusinessSalesProcessResult> findPageBySpec(CrmBusinessSalesProcessParam param);

}

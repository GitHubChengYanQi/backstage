package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSales;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 销售 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-02
 */
public interface CrmBusinessSalesService extends IService<CrmBusinessSales> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-02
     */
    void add(CrmBusinessSalesParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-02
     */
    void delete(CrmBusinessSalesParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-02
     */
    void update(CrmBusinessSalesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
    CrmBusinessSalesResult findBySpec(CrmBusinessSalesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
    List<CrmBusinessSalesResult> findListBySpec(CrmBusinessSalesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-02
     */
     PageInfo<CrmBusinessSalesResult> findPageBySpec(CrmBusinessSalesParam param);

}

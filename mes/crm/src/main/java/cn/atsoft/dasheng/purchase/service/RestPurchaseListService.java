package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseList;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 预购管理 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
public interface RestPurchaseListService extends IService<PurchaseList> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    void add(PurchaseListParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    void delete(PurchaseListParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    void update(PurchaseListParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    PurchaseListResult findBySpec(PurchaseListParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    List<PurchaseListResult> findListBySpec(PurchaseListParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
     PageInfo<PurchaseListResult> findPageBySpec(PurchaseListParam param);

}

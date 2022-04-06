package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.pojo.PlanListParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 采购清单 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
public interface PurchaseListingService extends IService<PurchaseListing> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-15
     */
    void add(PurchaseListingParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-15
     */
    void delete(PurchaseListingParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-15
     */
    void update(PurchaseListingParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    PurchaseListingResult findBySpec(PurchaseListingParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    List<PurchaseListingResult> findListBySpec(PurchaseListingParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    PageInfo<PurchaseListingResult> findPageBySpec(PurchaseListingParam param);

    List<PurchaseListingResult> getByAskId(Long askId);

    PageInfo<PurchaseListingResult> readyBuy(PurchaseListingParam param);

    /**
     * 待买
     *
     * @return
     */
    Set<ListingPlan> plans(PlanListParam param);

    void format(List<PurchaseListingResult> param);
}

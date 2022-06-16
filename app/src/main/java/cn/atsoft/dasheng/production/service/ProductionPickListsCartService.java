package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单详情表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface ProductionPickListsCartService extends IService<ProductionPickListsCart> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void add(ProductionPickListsCartParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void delete(ProductionPickListsCartParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void update(ProductionPickListsCartParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    ProductionPickListsCartResult findBySpec(ProductionPickListsCartParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<ProductionPickListsCartResult> findListBySpec(ProductionPickListsCartParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
     PageInfo<ProductionPickListsCartResult> findPageBySpec(ProductionPickListsCartParam param);

    void format(List<ProductionPickListsCartResult> param);

    List<CartGroupByUserListRequest> groupByUser(ProductionPickListsCartParam param);

    List<ProductionPickListsCartResult> getSelfCarts(ProductionPickListsCartParam param);

    List<ProductionPickListsResult> getSelfCartsByLists(ProductionPickListsCartParam param);

    List<Map<String,Object>> getSelfCartsBySku(ProductionPickListsCartParam productionPickListsCartParam);

    void deleteBatchByIds(List<Long> ids);
}

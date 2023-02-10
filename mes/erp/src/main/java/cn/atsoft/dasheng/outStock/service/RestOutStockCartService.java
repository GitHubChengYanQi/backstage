package cn.atsoft.dasheng.outStock.service;

//import cn.atsoft.dasheng.app.entity.RestStockDetails;
//import cn.atsoft.dasheng.app.model.result.RestStockDetailsResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.outStock.entity.RestOutStockCart;
//import cn.atsoft.dasheng.production.entity.RestOutStockCart;
//import cn.atsoft.dasheng.production.model.params.RestOutStockCartParam;
//import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
//import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
//import cn.atsoft.dasheng.production.model.result.PickListsStorehouseResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockCartResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderDetailResult;
//import cn.atsoft.dasheng.production.pojo.LockedRestStockDetails;
//import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockCartParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockCartResult;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockOrderDetailResult;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockDetail.model.result.RestStockDetailsResult;
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
public interface RestOutStockCartService extends IService<RestOutStockCart> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void add(RestOutStockCartParam param, List<RestStockDetails> stockDetails);

//    List<RestOutStockOrderDetailResult> autoAdd(RestOutStockCartParam param);
//
//    boolean warning(RestOutStockCartParam param);
//
//    void addCheck(RestOutStockCartParam param);
//
//    /**
//     * 删除
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    void delete(RestOutStockCartParam param);
//
//    /**
//     * 更新
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    void update(RestOutStockCartParam param);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    RestOutStockCartResult findBySpec(RestOutStockCartParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    List<RestOutStockCartResult> findListBySpec(RestOutStockCartParam param);
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//     PageInfo<RestOutStockCartResult> findPageBySpec(RestOutStockCartParam param);
//
////    List<LockedRestStockDetails> getLockSkuAndNumber(List<Long> skuIds);
//
////    void format(List<RestOutStockCartResult> param);
//
////    List<CartGroupByUserListRequest> groupByUser(RestOutStockCartParam param);
//
////    List<RestOutStockCartResult> getSelfCarts(RestOutStockCartParam param);
//
////    List<RestOutStockOrderDetailResult> getSelfCartsByLists(Long id);
//
////    List<Map<String, Object>> getSelfCartsBySku(RestOutStockCartParam productionPickListsCartParam);
//
////    void deleteBatchByIds(List<RestOutStockCartParam> param);
//
////    List<RestOutStockCartResult> listByListsDetailIds(List<Long> listsDetailIds);
//
////    List<RestStockDetails>getLockStockDetail();
//
////    List<Long> getLockedInkindIds();
//
////    Integer getLockNumber(QuerryLockedParam param);
//
//
//    List<Long> getCartInkindIds(List<Long> skuIds);
//
////    List<PickListsStorehouseResult> listPickListsStorehouse(ProductionPickListsParam param);
//
////    List<RestStockDetailsResult> getCartInkindByLists(RestOutStockCartParam param);
}

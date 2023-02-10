package cn.atsoft.dasheng.outStock.service;

//import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
//import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.outStock.entity.RestOutStockOrderDetail;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrderDetailParam;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderDetailParam;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderDetailResult;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderDetailParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockOrderDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 领料单详情表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface RestOutStockOrderDetailService extends IService<RestOutStockOrderDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void add(RestOutStockOrderDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void delete(RestOutStockOrderDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void update(RestOutStockOrderDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    RestOutStockOrderDetailResult findBySpec(RestOutStockOrderDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<RestOutStockOrderDetailResult> findListBySpec(RestOutStockOrderDetailParam param);

    List<RestOutStockOrderDetailResult> listStatus0ByPickLists(Long pickListsId);

    List<RestOutStockOrderDetailResult> listStatus0ByPickLists(List<Long> pickListsIds);

    List<RestOutStockOrderDetailResult> listByPickLists(List<Long> pickListsIds);

    List<Long> getSkuIdsByPickLists(Long id);

    List<RestOutStockOrderDetailResult> resultsByPickListsIds(List<Long> listsIds);

//    List<RestOutStockOrderDetailResult> pickListsTaskDetail(List<Long> pickListsIds);

//    void format(List<RestOutStockOrderDetailResult> results);

    List<RestOutStockOrderDetailResult> getByTask(RestOutStockOrderDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
     PageInfo<RestOutStockOrderDetailResult> findPageBySpec(RestOutStockOrderDetailParam param);

    PageInfo<RestOutStockOrderDetailResult> pickListsDetailList(RestOutStockOrderDetailParam param);

//    List<StockView> getUserSkuAndNumbers(DataStatisticsViewParam param);

    List<Long> getPisitionIds(Long listsId);
}

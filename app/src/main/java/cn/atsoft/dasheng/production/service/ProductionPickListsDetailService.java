package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
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
public interface ProductionPickListsDetailService extends IService<ProductionPickListsDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void add(ProductionPickListsDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void delete(ProductionPickListsDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void update(ProductionPickListsDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    ProductionPickListsDetailResult findBySpec(ProductionPickListsDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<ProductionPickListsDetailResult> findListBySpec(ProductionPickListsDetailParam param);

    List<ProductionPickListsDetailResult> listStatus0ByPickLists(Long pickListsId);

    List<ProductionPickListsDetailResult> listStatus0ByPickLists(List<Long> pickListsIds);

    List<ProductionPickListsDetailResult> resultsByPickLists(List<Long> pickListsIds);
    List<ProductionPickListsDetail> listByPickLists(List<Long> pickListsIds);

    List<Long> getSkuIdsByPickLists(Long id);

    List<ProductionPickListsDetailResult> resultsByPickListsIds(List<Long> listsIds);

    List<ProductionPickListsDetailResult> pickListsTaskDetail(List<Long> pickListsIds);

    void format(List<ProductionPickListsDetailResult> results);

    List<ProductionPickListsDetailResult> getByTask(ProductionPickListsDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
     PageInfo<ProductionPickListsDetailResult> findPageBySpec(ProductionPickListsDetailParam param);

    PageInfo<ProductionPickListsDetailResult> pickListsDetailList(ProductionPickListsDetailParam param);

    List<StockView> getUserSkuAndNumbers(DataStatisticsViewParam param);

    List<Long> getPisitionIds(Long listsId);
}

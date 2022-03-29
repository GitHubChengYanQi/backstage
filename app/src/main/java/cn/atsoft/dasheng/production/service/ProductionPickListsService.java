package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 领料单 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface ProductionPickListsService extends IService<ProductionPickLists> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void add(ProductionPickListsParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void delete(ProductionPickListsParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    void update(ProductionPickListsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    ProductionPickListsResult findBySpec(ProductionPickListsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<ProductionPickListsResult> findListBySpec(ProductionPickListsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
     PageInfo<ProductionPickListsResult> findPageBySpec(ProductionPickListsParam param);

    void format(List<ProductionPickListsResult> results);

    void formatStatus99(List<ProductionPickListsResult> results);

    String addByProductionTask(Object param);

    List<StorehouseResult> getStockSkus(List<Long> skuIds);

    void outStock(ProductionPickListsParam param);
}

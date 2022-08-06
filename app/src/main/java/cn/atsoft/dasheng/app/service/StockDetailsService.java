package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库物品明细表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
public interface StockDetailsService extends IService<StockDetails> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-15
     */
    Long add(StockDetailsParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-15
     */
    void delete(StockDetailsParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-15
     */
    void update(StockDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    StockDetailsResult findBySpec(StockDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    List<StockDetailsResult> findListBySpec(StockDetailsParam param);

    List<StockDetails> getStock();

    List<StockDetailsResult> getDetailsBySkuId(Long id);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    PageInfo<StockDetailsResult> findPageBySpec(StockDetailsParam param, DataScope dataScope);

    /**
     * 通过仓库返回所有sku
     *
     * @param id
     * @return
     */
    List<Long> backSkuByStoreHouse(Long id);

    void startAnalyse();

    void splitInKind(Long inKind);

    List<StockDetailsResult> inkindList(Long skuId);

    /**
     * 返回当前仓库下所有数据
     *
     * @param stockId
     * @return
     */
    List<StockDetailsResult> getStockDetails(Long stockId);

    /**
     * 预购返回库存数量
     *
     * @param plans
     */
    void preorder(List<ListingPlan> plans);


    List<StockSkuBrand> stockSkuBrands();

    Integer getNumberByStock(Long skuId, Long brandId, Long positionId);

    List<StockDetailsResult> getStockNumberBySkuId(Long skuId,Long storehouseId);

    List<StockSkuBrand> stockSku();

    void format(List<StockDetailsResult> data);

    List<StockDetailExcel> getStockDetail();

    List<StockDetails> maintenanceQuerry(StockDetailsParam param);

    StockDetails getInkind(StockDetailsParam param);

    List<StockDetails> fundStockDetailByCart(ProductionPickListsCartParam param);
}

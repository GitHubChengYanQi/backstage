package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
import cn.atsoft.dasheng.app.model.params.InventoryCorrectionParam;
import cn.atsoft.dasheng.app.model.request.StockDetailView;
import cn.atsoft.dasheng.app.model.result.ViewCountResult;
import cn.atsoft.dasheng.app.pojo.SpuClassDetail;
import cn.atsoft.dasheng.app.pojo.StockCensus;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    void addStockDetials(StockDetailsParam param);

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

    PageInfo<StockDetailsResult> skuDetailView(StockDetailsParam param, DataScope dataScope);

    List<StockDetails> getStock();

    List<StockDetailsResult> getDetailsBySkuId(Long id);

    List<StockDetailsResult> stockInKindList();

    List<SpuClassDetail> detailed();

    List<StockCensus> stockCensus();

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    PageInfo findPageBySpec(StockDetailsParam param, DataScope dataScope);

    /**
     * 通过仓库返回所有sku
     *
     * @param id
     * @return
     */
    List<Long> backSkuByStoreHouse(Long id);


    void statement();

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

    List<StockDetailsResult> getStockNumberBySkuId(StockDetailsParam stockDetailsParam);

    List<StockSkuBrand> stockSku();

    void format(List<StockDetailsResult> data);

    List<StockDetailExcel> getStockDetail();

    List<StockDetails> maintenanceQuerry(StockDetailsParam param);

    StockDetails getInkind(StockDetailsParam param);

    List<StockDetails> fundStockDetailByCart(ProductionPickListsCartParam param);

    List<StockDetailView> stockDetailViews();

    Long getNumberCountBySkuId(Long skuId);

    List<StockDetails> getNumberCountEntityBySkuIds(List<Long> skuIds);

    Integer getAllStockNumber();

    void InventoryCorrection(List<InventoryCorrectionParam> params);

    ViewCountResult getViewCount();

    List<StockDetails> getStockNumber(List<InventoryCorrectionParam> params);
    List<StockDetails> getStockNumberDetail(List<InventoryCorrectionParam> params);
}

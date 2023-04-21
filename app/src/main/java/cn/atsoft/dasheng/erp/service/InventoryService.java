package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盘点任务主表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
public interface InventoryService extends IService<Inventory> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    Inventory add(InventoryParam param);

    List<InventoryResult> listByTime();

    void timelyAdd(InventoryParam param);

    void staticState();

    void selectCondition(InventoryParam param);

    Inventory InventoryApply(InventoryParam param);

    List<InventoryStock> condition(InventoryDetailParam detailParam);

    InventoryDetailResult conditionGetOne(InventoryDetailParam detailParam);

    List<SkuBind> getSkuBinds(InventoryDetailParam detailParam);

    Object timely(Long positionId);

    void bySku(InventoryParam param);

    void updateStatus(ActivitiProcessTask processTask);

    Map<String, Object> detailBackMap(Long id);

    InventoryResult detail(Long id);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    void delete(InventoryParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    void update(InventoryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    InventoryResult findBySpec(InventoryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<InventoryResult> findListBySpec(InventoryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    PageInfo findPageBySpec(InventoryParam param, DataScope dataScope);

    PageInfo pageList(InventoryParam param, DataScope dataScope);

    /**
     * 扫码 盘点
     *
     * @param
     */
    void inventory(InventoryRequest inventoryRequest);


    InkindResult inkindInventory(Long id);

    StorehousePositionsResult positionInventory(Long id);

    void outUpdateStockDetail(Long skuId, Long brandId, Long realNumber);

    void inStockUpdateStock(Long skuId, Long brandId, Long customerId, Long positionId, Long realNumber);

    List<Long> timeOut(boolean timeOut);

    void format(List<InventoryResult> data);
}

package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    void add(InventoryParam param);

    void selectCondition(InventoryParam param);

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
    PageInfo<InventoryResult> findPageBySpec(InventoryParam param);

    /**
     * 扫码 盘点
     *
     * @param
     */
    void inventory(InventoryRequest inventoryRequest);


    InkindResult inkindInventory(Long id);

    StorehousePositionsResult positionInventory(Long id);
}

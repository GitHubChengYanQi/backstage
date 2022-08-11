package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.params.InventoryStockParam;
import cn.atsoft.dasheng.erp.model.result.InventoryStockResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存盘点处理 服务类
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
public interface InventoryStockService extends IService<InventoryStock> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-07-15
     */
    void add(InventoryStockParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-07-15
     */
    void delete(InventoryStockParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-07-15
     */
    void update(InventoryStockParam param);

    void addList(List<InventoryDetailParam> detailParams);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-07-15
     */
    InventoryStockResult findBySpec(InventoryStockParam param);

    Object taskList(Long inventoryId);

    Map<String, Integer> speedProgress(Long inventoryId);

    Map<String, Object>  orderDetail(Long instockOrderId);

    List<StorehousePositionsResult> positionsResultList(List<InventoryStockResult> detailResults);

    void updateStatus(List<Long> ids);

    /**
     * 修改状态
     *
     * @param param
     * @param status
     */

    void updateInventoryStatus(AnomalyParam param, int status);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-07-15
     */
    List<InventoryStockResult> findListBySpec(InventoryStockParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-07-15
     */
    PageInfo<InventoryStockResult> findPageBySpec(InventoryStockParam param);

    void format(List<InventoryStockResult> data);
}

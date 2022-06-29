package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 盘点任务详情 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
public interface InventoryDetailService extends IService<InventoryDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    void add(InventoryDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    void delete(InventoryDetailParam param);

    void inventoryInstock(InventoryDetailParam inventoryDetailParam);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    void update(InventoryDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    InventoryDetailResult findBySpec(InventoryDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<InventoryDetailResult> findListBySpec(InventoryDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
     PageInfo<InventoryDetailResult> findPageBySpec(InventoryDetailParam param);

}

package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Allocation;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.result.AllocationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 调拨主表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
public interface AllocationService extends IService<Allocation> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void add(AllocationParam param);

    void createPickListsAndInStockOrder(Long allocationId);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void delete(AllocationParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void update(AllocationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    AllocationResult findBySpec(AllocationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    List<AllocationResult> findListBySpec(AllocationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
     PageInfo<AllocationResult> findPageBySpec(AllocationParam param);

}

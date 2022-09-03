package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Allocation;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.result.AllocationResult;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
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
    Allocation add(AllocationParam param);

    void checkCart(Long allocation);

    void checkCarry(Long allocation);


    void createPickListsAndInStockOrder(AllocationParam param, List<AllocationCart> allocationCarts);

    void format(List<AllocationResult> data);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void delete(AllocationParam param);

    AllocationResult detail(Long allocationId);

    void newTransfer(AllocationCartParam param);

    void transferInStorehouse(AllocationCartParam param);

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
    PageInfo findPageBySpec(AllocationParam param);

    void createOrder(AllocationParam param);

    void checkCartDone(Long allocationId, List<InstockList> listing);

    List<InstockListResult> getInstockListResultsByAllocationTask(Long taskId);
}

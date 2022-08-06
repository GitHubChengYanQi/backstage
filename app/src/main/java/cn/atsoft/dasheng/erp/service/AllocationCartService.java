package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.result.AllocationCartResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 调拨子表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-25
 */
public interface AllocationCartService extends IService<AllocationCart> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void add(AllocationCartParam param);

    void addBatch(AllocationCartParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void delete(AllocationCartParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void update(AllocationCartParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    AllocationCartResult findBySpec(AllocationCartParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<AllocationCartResult> findListBySpec(AllocationCartParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
     PageInfo<AllocationCartResult> findPageBySpec(AllocationCartParam param);

    List<AllocationCartResult> resultsByAllocationId(Long allocationId);
}

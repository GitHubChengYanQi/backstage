package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 调拨子表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
public interface AllocationDetailService extends IService<AllocationDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void add(AllocationDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void delete(AllocationDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    void update(AllocationDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    AllocationDetailResult findBySpec(AllocationDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    List<AllocationDetailResult> findListBySpec(AllocationDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
     PageInfo<AllocationDetailResult> findPageBySpec(AllocationDetailParam param);

    List<AllocationDetailResult> resultsByAllocationId(Long allocationId);
}

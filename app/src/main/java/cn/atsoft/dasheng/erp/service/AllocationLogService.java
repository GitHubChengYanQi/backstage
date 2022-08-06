package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLog;
import cn.atsoft.dasheng.erp.model.params.AllocationLogParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-25
 */
public interface AllocationLogService extends IService<AllocationLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void add(AllocationLogParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void delete(AllocationLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    void update(AllocationLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    AllocationLogResult findBySpec(AllocationLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<AllocationLogResult> findListBySpec(AllocationLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
     PageInfo<AllocationLogResult> findPageBySpec(AllocationLogParam param);

}

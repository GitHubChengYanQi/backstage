package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLogDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
public interface AllocationLogDetailService extends IService<AllocationLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void add(AllocationLogDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void delete(AllocationLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void update(AllocationLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    AllocationLogDetailResult findBySpec(AllocationLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<AllocationLogDetailResult> findListBySpec(AllocationLogDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
     PageInfo<AllocationLogDetailResult> findPageBySpec(AllocationLogDetailParam param);

}

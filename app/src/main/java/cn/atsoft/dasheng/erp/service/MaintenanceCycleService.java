package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceCycle;
import cn.atsoft.dasheng.erp.model.params.MaintenanceCycleParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceCycleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物料维护周期 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-08
 */
public interface MaintenanceCycleService extends IService<MaintenanceCycle> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    void add(MaintenanceCycleParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    void delete(MaintenanceCycleParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    void update(MaintenanceCycleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    MaintenanceCycleResult findBySpec(MaintenanceCycleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    List<MaintenanceCycleResult> findListBySpec(MaintenanceCycleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
     PageInfo<MaintenanceCycleResult> findPageBySpec(MaintenanceCycleParam param);

}

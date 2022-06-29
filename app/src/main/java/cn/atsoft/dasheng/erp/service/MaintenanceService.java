package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 养护申请主表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
public interface MaintenanceService extends IService<Maintenance> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void add(MaintenanceParam param);

    List<StockDetails> needMaintenanceByRequirement(Maintenance param);

    List<Maintenance> findTaskByTime();

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void delete(MaintenanceParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void update(MaintenanceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    MaintenanceResult findBySpec(MaintenanceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<MaintenanceResult> findListBySpec(MaintenanceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
     PageInfo<MaintenanceResult> findPageBySpec(MaintenanceParam param);

    void format(List<MaintenanceResult> param);
}

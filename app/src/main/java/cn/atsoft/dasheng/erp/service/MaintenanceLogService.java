package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceLog;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 养护记录 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
public interface MaintenanceLogService extends IService<MaintenanceLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void add(MaintenanceLogParam param);

    MaintenanceLogResult detail(Long id);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void delete(MaintenanceLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void update(MaintenanceLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    MaintenanceLogResult findBySpec(MaintenanceLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<MaintenanceLogResult> findListBySpec(MaintenanceLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
     PageInfo<MaintenanceLogResult> findPageBySpec(MaintenanceLogParam param);

    MaintenanceLogResult detail(Long id);
}

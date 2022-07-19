package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceDetailResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 养护申请子表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
public interface MaintenanceDetailService extends IService<MaintenanceDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void add(MaintenanceDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void delete(MaintenanceDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    void update(MaintenanceDetailParam param);

    List<StockDetailsResult> needMaintenance(List<Long> ids);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    MaintenanceDetailResult findBySpec(MaintenanceDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<MaintenanceDetailResult> findListBySpec(MaintenanceDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
     PageInfo<MaintenanceDetailResult> findPageBySpec(MaintenanceDetailParam param);

    void format(List<MaintenanceDetailResult> data);
}

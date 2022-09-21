package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.entity.MaintenanceLogDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogDetailParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogDetailResult;
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
public interface MaintenanceLogDetailService extends IService<MaintenanceLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void add(MaintenanceLogDetailParam param);

    void processingData(MaintenanceLogParam param, List<StockDetails> stockDetails, List<MaintenanceDetail> maintenanceDetails, List<MaintenanceLogDetail> logs);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void delete(MaintenanceLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    void update(MaintenanceLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    MaintenanceLogDetailResult findBySpec(MaintenanceLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<MaintenanceLogDetailResult> findListBySpec(MaintenanceLogDetailParam param);

    List<MaintenanceLogDetailResult> lastLogByInkindIds(List<Long> inkindIds);

    void format(List<MaintenanceLogDetailResult> data);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
     PageInfo<MaintenanceLogDetailResult> findPageBySpec(MaintenanceLogDetailParam param);

}

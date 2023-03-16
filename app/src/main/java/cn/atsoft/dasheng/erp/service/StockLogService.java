package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLog;
import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.model.params.StockLogParam;
import cn.atsoft.dasheng.erp.model.result.StockLogResult;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库存操作记录主表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
public interface StockLogService extends IService<StockLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void add(StockLogParam param);

    void addBatch(List<StockDetails> param, String source, String type, ProcessType numberType);

    void addByOutStock(List<StockLog> stockLogs, List<StockLogDetail> stockLogDetails);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void delete(StockLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void update(StockLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    StockLogResult findBySpec(StockLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<StockLogResult> findListBySpec(StockLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
     PageInfo<StockLogResult> findPageBySpec(StockLogParam param);

    Integer todayInStockNumber();

    Integer todayOutStockNumber();
}

package cn.atsoft.dasheng.stockLog.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockLog.entity.RestStockLog;
import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogResult;
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
public interface RestStockLogService extends IService<RestStockLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void add(RestStockLogParam param);

    void addBatch(List<RestStockLogParam> param);

    void addBatch(List<RestStockDetails> param,String type,String source);


    void addByOutStock(List<RestStockLog> restStockLogs, List<RestStockLogDetail> restStockLogDetails);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void delete(RestStockLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void update(RestStockLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    RestStockLogResult findBySpec(RestStockLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<RestStockLogResult> findListBySpec(RestStockLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
     PageInfo<RestStockLogResult> findPageBySpec(RestStockLogParam param);

}

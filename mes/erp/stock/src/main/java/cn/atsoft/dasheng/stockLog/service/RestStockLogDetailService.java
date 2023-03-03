package cn.atsoft.dasheng.stockLog.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogDetailParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库存操作记录子表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
public interface RestStockLogDetailService extends IService<RestStockLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void add(RestStockLogDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void delete(RestStockLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void update(RestStockLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    RestStockLogDetailResult findBySpec(RestStockLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<RestStockLogDetailResult> findListBySpec(RestStockLogDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
     PageInfo<RestStockLogDetailResult> findPageBySpec(RestStockLogDetailParam param);

}

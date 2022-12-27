package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.model.params.StockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.StockLogDetailResult;
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
public interface StockLogDetailService extends IService<StockLogDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void add(StockLogDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void delete(StockLogDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    void update(StockLogDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    StockLogDetailResult findBySpec(StockLogDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<StockLogDetailResult> findListBySpec(StockLogDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
     PageInfo<StockLogDetailResult> findPageBySpec(StockLogDetailParam param);

}

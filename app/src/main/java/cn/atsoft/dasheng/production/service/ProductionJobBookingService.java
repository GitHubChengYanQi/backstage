package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBooking;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报工表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
public interface ProductionJobBookingService extends IService<ProductionJobBooking> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void add(ProductionJobBookingParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void delete(ProductionJobBookingParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void update(ProductionJobBookingParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    ProductionJobBookingResult findBySpec(ProductionJobBookingParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<ProductionJobBookingResult> findListBySpec(ProductionJobBookingParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
     PageInfo<ProductionJobBookingResult> findPageBySpec(ProductionJobBookingParam param);

}

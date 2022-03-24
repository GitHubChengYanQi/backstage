package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.request.JobBookingDetailCount;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报工详情表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
public interface ProductionJobBookingDetailService extends IService<ProductionJobBookingDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void add(ProductionJobBookingDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void delete(ProductionJobBookingDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    void update(ProductionJobBookingDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    ProductionJobBookingDetailResult findBySpec(ProductionJobBookingDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<ProductionJobBookingDetailResult> findListBySpec(ProductionJobBookingDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
     PageInfo<ProductionJobBookingDetailResult> findPageBySpec(ProductionJobBookingDetailParam param);

    List<ProductionJobBookingDetailResult> resultsByJobBookingIds(List<Long> jobBookingIds);

    List<JobBookingDetailCount> resultsByProductionTaskIds(List<Long> productionTaskIds);
}

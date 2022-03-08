package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产计划子表 服务类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
public interface ProductionPlanDetailService extends IService<ProductionPlanDetail> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-25
     */
    void add(ProductionPlanDetailParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-25
     */
    void delete(ProductionPlanDetailParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-25
     */
    void update(ProductionPlanDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
    ProductionPlanDetailResult findBySpec(ProductionPlanDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
    List<ProductionPlanDetailResult> findListBySpec(ProductionPlanDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
     PageInfo<ProductionPlanDetailResult> findPageBySpec(ProductionPlanDetailParam param);

    List<ProductionPlanDetailResult> resultsByPlanIds(List<Long> planIds);
}

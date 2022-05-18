package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产计划主表 服务类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
public interface ProductionPlanService extends IService<ProductionPlan> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-25
     */
    void add(ProductionPlanParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-25
     */
    void delete(ProductionPlanParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-25
     */
    void update(ProductionPlanParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
    ProductionPlanResult findBySpec(ProductionPlanParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
    List<ProductionPlanResult> findListBySpec(ProductionPlanParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-25
     */
     PageInfo<ProductionPlanResult> findPageBySpec(ProductionPlanParam param);

    void format(List<ProductionPlanResult> param);

    List<ProductionPlanResult> resultsByIds(List<Long> ids);
}

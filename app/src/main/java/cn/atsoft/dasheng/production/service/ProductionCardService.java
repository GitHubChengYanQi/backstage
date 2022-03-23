package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产卡片 服务类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
public interface ProductionCardService extends IService<ProductionCard> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-28
     */
    void add(ProductionCardParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-28
     */
    void delete(ProductionCardParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-28
     */
    void update(ProductionCardParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    ProductionCardResult findBySpec(ProductionCardParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionCardResult> findListBySpec(ProductionCardParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
     PageInfo<ProductionCardResult> findPageBySpec(ProductionCardParam param);

    List<ProductionCard>  addBatchCardByProductionPlan(Object o);
}

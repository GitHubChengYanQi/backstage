package cn.atsoft.dasheng.production.card.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.card.entity.ProductionCard;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产卡片 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
public interface RestProductionCardService extends IService<ProductionCard> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void add(ProductionCardParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void delete(ProductionCardParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void update(ProductionCardParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    ProductionCardResult findBySpec(ProductionCardParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    List<ProductionCardResult> findListBySpec(ProductionCardParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
     PageInfo<ProductionCardResult> findPageBySpec(ProductionCardParam param);

}

package cn.atsoft.dasheng.production.card.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.card.entity.ProductionCardBind;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardBindParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产卡片绑定 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
public interface RestProductionCardBindService extends IService<ProductionCardBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void add(ProductionCardBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void delete(ProductionCardBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    void update(ProductionCardBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    ProductionCardBindResult findBySpec(ProductionCardBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    List<ProductionCardBindResult> findListBySpec(ProductionCardBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
     PageInfo<ProductionCardBindResult> findPageBySpec(ProductionCardBindParam param);

}

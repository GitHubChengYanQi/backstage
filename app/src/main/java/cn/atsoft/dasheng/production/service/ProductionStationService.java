package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工位表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface ProductionStationService extends IService<ProductionStation> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(ProductionStationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(ProductionStationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(ProductionStationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    ProductionStationResult findBySpec(ProductionStationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<ProductionStationResult> findListBySpec(ProductionStationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
     PageInfo<ProductionStationResult> findPageBySpec(ProductionStationParam param);

    void format(List<ProductionStationResult> data);

    List<ProductionStationResult> getResultsByIds(List<Long> ids);
}

package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationBind;
import cn.atsoft.dasheng.production.model.params.ProductionStationBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工位绑定表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
public interface ProductionStationBindService extends IService<ProductionStationBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void add(ProductionStationBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void delete(ProductionStationBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void update(ProductionStationBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    ProductionStationBindResult findBySpec(ProductionStationBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<ProductionStationBindResult> findListBySpec(ProductionStationBindParam param);

    List<ProductionStationBindResult> getResultsByStationIds(List<Long> ids);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
     PageInfo<ProductionStationBindResult> findPageBySpec(ProductionStationBindParam param);

}

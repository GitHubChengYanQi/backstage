package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.model.params.ProductionStationClassParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工位绑定表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface ProductionStationClassService extends IService<ProductionStationClass> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(ProductionStationClassParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(ProductionStationClassParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(ProductionStationClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    ProductionStationClassResult findBySpec(ProductionStationClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<ProductionStationClassResult> findListBySpec(ProductionStationClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
     PageInfo<ProductionStationClassResult> findPageBySpec(ProductionStationClassParam param);

}

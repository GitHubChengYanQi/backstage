package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockForewarn;
import cn.atsoft.dasheng.erp.model.params.StockForewarnParam;
import cn.atsoft.dasheng.erp.model.result.StockForewarnResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存预警设置 服务类
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
public interface StockForewarnService extends IService<StockForewarn> {

    /**
     * 新增
     *
     * @author sjl
     * @Date 2022-12-05
     */
    void add(StockForewarnParam param);

    void saveOrUpdateByat(List<StockForewarnParam> params);

    /**
     * 删除
     *
     * @author sjl
     * @Date 2022-12-05
     */
    void delete(StockForewarnParam param);

    /**
     * 批量保存
     */
    void saveBatchByBomId(Long bomId, StockForewarnParam param);

    /**
     * 更新
     *
     * @author sjl
     * @Date 2022-12-05
     */
    void update(StockForewarnParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author sjl
     * @Date 2022-12-05
     */
    StockForewarnResult findBySpec(StockForewarnParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author sjl
     * @Date 2022-12-05
     */
    List<StockForewarnResult> findListBySpec(StockForewarnParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author sjl
     * @Date 2022-12-05
     */
     PageInfo<StockForewarnResult> findPageBySpec(StockForewarnParam param);

    PageInfo showWaring(StockForewarnParam param);

    List<StockForewarnResult> listBySkuIds(List<Long> skuIds);

    Map<String,Object> view(Long tenantId);
}

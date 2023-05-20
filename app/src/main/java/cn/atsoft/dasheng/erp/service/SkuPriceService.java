package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuPrice;
import cn.atsoft.dasheng.erp.model.params.SkuPriceParam;
import cn.atsoft.dasheng.erp.model.result.SkuPriceListResult;
import cn.atsoft.dasheng.erp.model.result.SkuPriceResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品价格设置表 服务类
 * </p>
 *
 * @author sjl
 * @since 2023-01-09
 */
public interface SkuPriceService extends IService<SkuPrice> {

    /**
     * 新增
     *
     * @author sjl
     * @Date 2023-01-09
     */
    void add(SkuPriceParam param);

    void messageAdd(SkuPriceParam param);

    /***
     * 批量
     */
    void addBatch(List<SkuPriceParam> params);

    void delete(SkuPriceParam param);

    void update(SkuPriceParam param);

    /**
     * 返回值
     */
    List<SkuPriceListResult> skuPriceResultBySkuIds(List<Long> skuIds);

    /**
     * 查询单条数据，Specification模式
     *
     * @author sjl
     * @Date 2023-01-09
     */
    SkuPriceResult findBySpec(SkuPriceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author sjl
     * @Date 2023-01-09
     */
    List<SkuPriceResult> findListBySpec(SkuPriceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author sjl
     * @Date 2023-01-09
     */
    PageInfo<SkuPriceResult> findPageBySpec(SkuPriceParam param);

}

package cn.atsoft.dasheng.goods.brand.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.brand.entity.RestSkuBrandBind;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestSkuBrandBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-01-18
 */
public interface RestSkuBrandBindService extends IService<RestSkuBrandBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void add(RestSkuBrandBindParam param);

    void addBatch(RestSkuBrandBindParam param);

    void addBatchByBrand(RestSkuBrandBindParam param);
    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void delete(RestSkuBrandBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void update(RestSkuBrandBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    RestSkuBrandBindResult findBySpec(RestSkuBrandBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<RestSkuBrandBindResult> findListBySpec(RestSkuBrandBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
     PageInfo<RestSkuBrandBindResult> findPageBySpec(RestSkuBrandBindParam param);

}

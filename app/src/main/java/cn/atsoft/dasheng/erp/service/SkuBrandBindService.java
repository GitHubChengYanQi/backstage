package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.params.SkuBrandBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuBrandBindResult;
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
public interface SkuBrandBindService extends IService<SkuBrandBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void add(SkuBrandBindParam param);

    void addBatch(SkuBrandBindParam param);

    void addBatchByBrand(SkuBrandBindParam param);
    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void delete(SkuBrandBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    void update(SkuBrandBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    SkuBrandBindResult findBySpec(SkuBrandBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<SkuBrandBindResult> findListBySpec(SkuBrandBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
     PageInfo<SkuBrandBindResult> findPageBySpec(SkuBrandBindParam param);

}

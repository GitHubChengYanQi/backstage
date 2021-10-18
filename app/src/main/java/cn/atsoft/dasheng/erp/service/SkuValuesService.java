package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.model.params.SkuValuesParam;
import cn.atsoft.dasheng.erp.model.result.SkuValuesResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku详情表 服务类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
public interface SkuValuesService extends IService<SkuValues> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-18
     */
    void add(SkuValuesParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-18
     */
    void delete(SkuValuesParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-18
     */
    void update(SkuValuesParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
    SkuValuesResult findBySpec(SkuValuesParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
    List<SkuValuesResult> findListBySpec(SkuValuesParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
     PageInfo<SkuValuesResult> findPageBySpec(SkuValuesParam param);

}

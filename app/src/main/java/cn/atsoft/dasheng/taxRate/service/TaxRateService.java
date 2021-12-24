package cn.atsoft.dasheng.taxRate.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.model.params.TaxRateParam;
import cn.atsoft.dasheng.taxRate.model.result.TaxRateResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-12-21
 */
public interface TaxRateService extends IService<TaxRate> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-12-21
     */
    void add(TaxRateParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-12-21
     */
    void delete(TaxRateParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-12-21
     */
    void update(TaxRateParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-12-21
     */
    TaxRateResult findBySpec(TaxRateParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-12-21
     */
    List<TaxRateResult> findListBySpec(TaxRateParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-12-21
     */
     PageInfo<TaxRateResult> findPageBySpec(TaxRateParam param);

}

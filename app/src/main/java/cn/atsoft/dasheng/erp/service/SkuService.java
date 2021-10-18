package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku表	 服务类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
public interface SkuService extends IService<Sku> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-18
     */
    void add(SkuParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-18
     */
    void delete(SkuParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-18
     */
    void update(SkuParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
    SkuResult findBySpec(SkuParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
    List<SkuResult> findListBySpec(SkuParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-18
     */
     PageInfo<SkuResult> findPageBySpec(SkuParam param);

}

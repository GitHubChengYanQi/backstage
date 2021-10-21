package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品订单详情 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
public interface ProductOrderDetailsService extends IService<ProductOrderDetails> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-20
     */
    void add(ProductOrderDetailsParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-20
     */
    void delete(ProductOrderDetailsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-20
     */
    void update(ProductOrderDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
    ProductOrderDetailsResult findBySpec(ProductOrderDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
    List<ProductOrderDetailsResult> findListBySpec(ProductOrderDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
     PageInfo<ProductOrderDetailsResult> findPageBySpec(ProductOrderDetailsParam param);

}

package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品订单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
public interface ProductOrderService extends IService<ProductOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-20
     */
    void add(ProductOrderParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-20
     */
    void delete(ProductOrderParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-20
     */
    void update(ProductOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
    ProductOrderResult findBySpec(ProductOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
    List<ProductOrderResult> findListBySpec(ProductOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-20
     */
     PageInfo<ProductOrderResult> findPageBySpec(ProductOrderParam param);

}

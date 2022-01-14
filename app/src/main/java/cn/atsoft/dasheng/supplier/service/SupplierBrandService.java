package cn.atsoft.dasheng.supplier.service;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.supplier.entity.SupplierBrand;
import cn.atsoft.dasheng.supplier.model.params.SupplierBrandParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBrandResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商品牌绑定 服务类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface SupplierBrandService extends IService<SupplierBrand> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-01-13
     */
    void add(SupplierBrandParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-01-13
     */
    void delete(SupplierBrandParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-01-13
     */
    void update(SupplierBrandParam param);

    void getBrand(List<CustomerResult> customerResults);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    SupplierBrandResult findBySpec(SupplierBrandParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    List<SupplierBrandResult> findListBySpec(SupplierBrandParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
     PageInfo<SupplierBrandResult> findPageBySpec(SupplierBrandParam param);

}

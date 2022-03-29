package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCode;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 领取物料码 服务类
 * </p>
 *
 * @author cheng
 * @since 2022-03-29
 */
public interface ProductionPickCodeService extends IService<ProductionPickCode> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2022-03-29
     */
    void add(ProductionPickCodeParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2022-03-29
     */
    void delete(ProductionPickCodeParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2022-03-29
     */
    void update(ProductionPickCodeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2022-03-29
     */
    ProductionPickCodeResult findBySpec(ProductionPickCodeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2022-03-29
     */
    List<ProductionPickCodeResult> findListBySpec(ProductionPickCodeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2022-03-29
     */
     PageInfo<ProductionPickCodeResult> findPageBySpec(ProductionPickCodeParam param);

}

package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCodeBind;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-29
 */
public interface ProductionPickCodeBindService extends IService<ProductionPickCodeBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    void add(ProductionPickCodeBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    void delete(ProductionPickCodeBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    void update(ProductionPickCodeBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    ProductionPickCodeBindResult findBySpec(ProductionPickCodeBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    List<ProductionPickCodeBindResult> findListBySpec(ProductionPickCodeBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
     PageInfo<ProductionPickCodeBindResult> findPageBySpec(ProductionPickCodeBindParam param);

}

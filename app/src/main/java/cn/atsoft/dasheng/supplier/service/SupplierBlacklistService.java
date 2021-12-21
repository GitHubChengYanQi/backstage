package cn.atsoft.dasheng.supplier.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.supplier.entity.SupplierBlacklist;
import cn.atsoft.dasheng.supplier.model.params.SupplierBlacklistParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBlacklistResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商黑名单 服务类
 * </p>
 *
 * @author Captian_Jazz
 * @since 2021-12-20
 */
public interface SupplierBlacklistService extends IService<SupplierBlacklist> {

    /**
     * 新增
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    void add(SupplierBlacklistParam param);

    /**
     * 删除
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    void delete(SupplierBlacklistParam param);

    /**
     * 更新
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    void update(SupplierBlacklistParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    SupplierBlacklistResult findBySpec(SupplierBlacklistParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    List<SupplierBlacklistResult> findListBySpec(SupplierBlacklistParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
     PageInfo<SupplierBlacklistResult> findPageBySpec(SupplierBlacklistParam param);

}

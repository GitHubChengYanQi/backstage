package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统租户表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-07
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    String  add(TenantParam param);

    String changeTenant(TenantParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    void delete(TenantParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    String update(TenantParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    TenantResult findBySpec(TenantParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    List<TenantResult> findListBySpec(TenantParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
     PageInfo<TenantResult> findPageBySpec(TenantParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
     PageInfo<TenantResult> findPageBySpec(TenantParam param,DataScope dataScope);

    void format(List<TenantResult> dataList);

    //写一个根据ids 取出result的方法
    List<TenantResult> getTenantResultsByIds(List<Long> ids);

    void changeUser(Long tenantId, Long userId);
}

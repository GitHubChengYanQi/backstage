package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 租户用户绑定表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-19
 */
public interface TenantBindService extends IService<TenantBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    void add(TenantBindParam param);

    Long getOrSave(TenantBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    void delete(TenantBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    void update(TenantBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    TenantBindResult findBySpec(TenantBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    List<TenantBindResult> findListBySpec(TenantBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
     PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
     PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param,DataScope dataScope);

}

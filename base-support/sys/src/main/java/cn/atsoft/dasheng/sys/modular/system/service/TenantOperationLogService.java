package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantOperationLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantOperationLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantOperationLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门造作记录表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
public interface TenantOperationLogService extends IService<TenantOperationLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void add(TenantOperationLogParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void delete(TenantOperationLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void update(TenantOperationLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    TenantOperationLogResult findBySpec(TenantOperationLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<TenantOperationLogResult> findListBySpec(TenantOperationLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<TenantOperationLogResult> findPageBySpec(TenantOperationLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<TenantOperationLogResult> findPageBySpec(TenantOperationLogParam param,DataScope dataScope);

}

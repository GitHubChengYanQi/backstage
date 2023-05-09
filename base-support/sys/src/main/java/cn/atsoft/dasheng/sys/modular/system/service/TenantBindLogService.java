package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBindLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 邀请记录  申请记录 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
public interface TenantBindLogService extends IService<TenantBindLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Long add(TenantBindLogParam param);

    Long addLog(Long userId, Long tenantId, Long deptId, String type);

    void updateStatus(Long tenantBindLogId, Integer status);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void delete(TenantBindLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void update(TenantBindLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    TenantBindLogResult findBySpec(TenantBindLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<TenantBindLogResult> findListBySpec(TenantBindLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<TenantBindLogResult> findPageBySpec(TenantBindLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<TenantBindLogResult> findPageBySpec(TenantBindLogParam param,DataScope dataScope);

}

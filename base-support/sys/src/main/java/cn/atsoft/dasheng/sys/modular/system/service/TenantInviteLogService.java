package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantInviteLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantInviteLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantInviteLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 邀请记录  申请记录 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-12
 */
public interface TenantInviteLogService extends IService<TenantInviteLog> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    Long add(TenantInviteLogParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    void delete(TenantInviteLogParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    void update(TenantInviteLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    TenantInviteLogResult findBySpec(TenantInviteLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    List<TenantInviteLogResult> findListBySpec(TenantInviteLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
     PageInfo<TenantInviteLogResult> findPageBySpec(TenantInviteLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
     PageInfo<TenantInviteLogResult> findPageBySpec(TenantInviteLogParam param,DataScope dataScope);

}

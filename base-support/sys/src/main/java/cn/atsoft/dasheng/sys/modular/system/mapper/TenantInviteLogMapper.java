package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantInviteLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantInviteLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantInviteLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 邀请记录  申请记录 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-12
 */
public interface TenantInviteLogMapper extends BaseMapper<TenantInviteLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    List<TenantInviteLogResult> customList(@Param("paramCondition") TenantInviteLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TenantInviteLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    Page<TenantInviteLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantInviteLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    Page<TenantInviteLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantInviteLogParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TenantInviteLogParam paramCondition);

}

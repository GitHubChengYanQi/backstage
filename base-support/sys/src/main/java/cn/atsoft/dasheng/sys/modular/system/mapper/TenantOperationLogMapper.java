package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantOperationLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantOperationLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantOperationLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门造作记录表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
public interface TenantOperationLogMapper extends BaseMapper<TenantOperationLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<TenantOperationLogResult> customList(@Param("paramCondition") TenantOperationLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TenantOperationLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<TenantOperationLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantOperationLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<TenantOperationLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantOperationLogParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TenantOperationLogParam paramCondition);

}

package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统租户表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-07
 */
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    List<TenantResult> customList(@Param("paramCondition") TenantParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TenantParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    Page<TenantResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    Page<TenantResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TenantParam paramCondition);

}

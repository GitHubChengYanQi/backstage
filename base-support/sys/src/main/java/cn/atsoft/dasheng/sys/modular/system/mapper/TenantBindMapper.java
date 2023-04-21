package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户用户绑定表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-19
 */
public interface TenantBindMapper extends BaseMapper<TenantBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    List<TenantBindResult> customList(@Param("paramCondition") TenantBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TenantBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    Page<TenantBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    Page<TenantBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantBindParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TenantBindParam paramCondition);

}

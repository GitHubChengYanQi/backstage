package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBindLog;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindLogResult;
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
 * @since 2023-05-09
 */
public interface TenantBindLogMapper extends BaseMapper<TenantBindLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<TenantBindLogResult> customList(@Param("paramCondition") TenantBindLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TenantBindLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<TenantBindLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantBindLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<TenantBindLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TenantBindLogParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TenantBindLogParam paramCondition);

}

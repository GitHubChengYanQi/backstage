package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.MenuConfig;
import cn.atsoft.dasheng.sys.modular.system.model.params.MenuConfigParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.MenuConfigResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单显示配置表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-28
 */
public interface MenuConfigMapper extends BaseMapper<MenuConfig> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    List<MenuConfigResult> customList(@Param("paramCondition") MenuConfigParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MenuConfigParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    Page<MenuConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") MenuConfigParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    Page<MenuConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") MenuConfigParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MenuConfigParam paramCondition);

}

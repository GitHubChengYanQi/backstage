package cn.atsoft.dasheng.db.mapper;

import cn.atsoft.dasheng.db.entity.FieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字段配置 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2020-12-12
 */
public interface FieldConfigMapper extends BaseMapper<FieldConfig> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2020-12-12
     */
    List<FieldConfigResult> customList(@Param("paramCondition") FieldConfigParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2020-12-12
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") FieldConfigParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2020-12-12
     */
    Page<FieldConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") FieldConfigParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2020-12-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") FieldConfigParam paramCondition);

}

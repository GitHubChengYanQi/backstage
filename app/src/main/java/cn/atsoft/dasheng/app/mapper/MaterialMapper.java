package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.model.params.MaterialParam;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 材质 Mapper 接口
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface MaterialMapper extends BaseMapper<Material> {

    /**
     * 获取列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<MaterialResult> customList(@Param("paramCondition") MaterialParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaterialParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<MaterialResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaterialParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaterialParam paramCondition);

}

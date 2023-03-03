package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.model.params.RestFormStyleParam;;
import cn.atsoft.dasheng.form.model.result.RestFormStyleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单风格 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-09-23
 */
public interface RestFormStyleMapper extends BaseMapper<RestFormStyle> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-09-23
     */
    List<RestFormStyleResult> customList(@Param("paramCondition") RestFormStyleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-09-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestFormStyleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-09-23
     */
    Page<RestFormStyleResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestFormStyleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-09-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestFormStyleParam paramCondition);

}

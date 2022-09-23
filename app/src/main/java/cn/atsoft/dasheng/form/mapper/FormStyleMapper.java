package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.model.params.FormStyleParam;
import cn.atsoft.dasheng.form.model.result.FormStyleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface FormStyleMapper extends BaseMapper<FormStyle> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-09-23
     */
    List<FormStyleResult> customList(@Param("paramCondition") FormStyleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-09-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") FormStyleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-09-23
     */
    Page<FormStyleResult> customPageList(@Param("page") Page page, @Param("paramCondition") FormStyleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-09-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") FormStyleParam paramCondition);

}

package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.params.FormDataValueParam;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单各个字段数据	 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormDataValueMapper extends BaseMapper<FormDataValue> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormDataValueResult> customList(@Param("paramCondition") FormDataValueParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") FormDataValueParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<FormDataValueResult> customPageList(@Param("page") Page page, @Param("paramCondition") FormDataValueParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") FormDataValueParam paramCondition);

}

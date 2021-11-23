package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.model.params.FormDataParam;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单主表 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface FormDataMapper extends BaseMapper<FormData> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<FormDataResult> customList(@Param("paramCondition") FormDataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") FormDataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<FormDataResult> customPageList(@Param("page") Page page, @Param("paramCondition") FormDataParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") FormDataParam paramCondition);

}

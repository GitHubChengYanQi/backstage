package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.RestGeneralFormData;
import cn.atsoft.dasheng.form.model.params.RestGeneralFormDataParam;
import cn.atsoft.dasheng.form.model.result.RestGeneralFormDataResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-09-08
 */
public interface RestGeneralFormDataMapper extends BaseMapper<RestGeneralFormData> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<RestGeneralFormDataResult> customList(@Param("paramCondition") RestGeneralFormDataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestGeneralFormDataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    Page<RestGeneralFormDataResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestGeneralFormDataParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestGeneralFormDataParam paramCondition);

}

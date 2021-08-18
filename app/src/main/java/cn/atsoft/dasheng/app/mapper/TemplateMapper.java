package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同模板 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-21
 */
public interface TemplateMapper extends BaseMapper<Template> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-21
     */
    List<TemplateResult> customList(@Param("paramCondition") TemplateParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TemplateParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-21
     */
    Page<TemplateResult> customPageList(@Param("page") Page page, @Param("paramCondition") TemplateParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TemplateParam paramCondition);

}

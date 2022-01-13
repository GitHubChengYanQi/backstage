package cn.atsoft.dasheng.printTemplate.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.params.PrintTemplateParam;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编辑模板 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
public interface PrintTemplateMapper extends BaseMapper<PrintTemplate> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    List<PrintTemplateResult> customList(@Param("paramCondition") PrintTemplateParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PrintTemplateParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    Page<PrintTemplateResult> customPageList(@Param("DateScope") DataScope dataScope, @Param("page") Page page, @Param("paramCondition") PrintTemplateParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PrintTemplateParam paramCondition);

}

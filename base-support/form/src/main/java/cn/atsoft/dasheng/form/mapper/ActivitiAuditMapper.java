package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批配置表 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiAuditMapper extends BaseMapper<ActivitiAudit> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiAuditResult> customList(@Param("paramCondition") ActivitiAuditParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ActivitiAuditParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<ActivitiAuditResult> customPageList(@Param("page") Page page, @Param("paramCondition") ActivitiAuditParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ActivitiAuditParam paramCondition);

}

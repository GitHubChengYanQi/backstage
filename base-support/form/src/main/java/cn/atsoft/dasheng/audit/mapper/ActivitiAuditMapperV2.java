package cn.atsoft.dasheng.audit.mapper;

import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.audit.entity.ActivitiAuditV2;
import cn.atsoft.dasheng.audit.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResultV2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ActivitiAuditMapperV2 extends BaseMapper<ActivitiAuditV2> {

//    /**
//     * 获取列表
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    List<ActivitiAuditResultV2> customList(@Param("paramCondition") ActivitiAuditParam paramCondition);
    List<ActivitiAuditV2> resultByIds(@Param("stepsIds") List<Long> stepsIds);
//
//    /**
//     * 获取map列表
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    List<Map<String, Object>> customMapList(@Param("paramCondition") ActivitiAuditParam paramCondition);
//
//    /**
//     * 获取分页实体列表
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    Page<ActivitiAuditResultV2> customPageList(@Param("page") Page page, @Param("paramCondition") ActivitiAuditParam paramCondition);
//
//    /**
//     * 获取分页map列表
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ActivitiAuditParam paramCondition);

}

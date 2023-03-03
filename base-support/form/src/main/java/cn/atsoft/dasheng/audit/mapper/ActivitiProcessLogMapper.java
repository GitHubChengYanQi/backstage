package cn.atsoft.dasheng.audit.mapper;

import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.audit.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程日志表 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiProcessLogMapper extends BaseMapper<ActivitiProcessLog> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessLogResult> customList(@Param("paramCondition") ActivitiProcessLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ActivitiProcessLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<ActivitiProcessLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") ActivitiProcessLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ActivitiProcessLogParam paramCondition);

    /**
     * 查询所有审核的
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessLogResult> auditList(@Param("stepIds") List<Long> stepIds, @Param("paramCondition") ActivitiProcessLogParam paramCondition);

    /**
     * 查出所有推送的
     *
     * @param stepIds
     * @param paramCondition
     * @return
     */
    List<ActivitiProcessLogResult> sendList(@Param("stepIds") List<Long> stepIds, @Param("paramCondition") ActivitiProcessLogParam paramCondition);
}

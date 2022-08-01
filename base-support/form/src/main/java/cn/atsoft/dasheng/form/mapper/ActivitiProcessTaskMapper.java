package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程任务表	 Mapper 接口
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
public interface ActivitiProcessTaskMapper extends BaseMapper<ActivitiProcessTask> {

    /**
     * 获取列表
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    List<ActivitiProcessTaskResult> customList(@Param("paramCondition") ActivitiProcessTaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ActivitiProcessTaskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    Page<ActivitiProcessTaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") ActivitiProcessTaskParam paramCondition);

    Page<ActivitiProcessTaskResult> selfPickListsTasks(@Param("page") Page page, @Param("paramCondition") ActivitiProcessTaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Jazz
     * @Date 2021-11-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ActivitiProcessTaskParam paramCondition);



    Page<ActivitiProcessTaskResult> auditList(@Param("page") Page page, @Param("paramCondition") ActivitiProcessTaskParam paramCondition);
}

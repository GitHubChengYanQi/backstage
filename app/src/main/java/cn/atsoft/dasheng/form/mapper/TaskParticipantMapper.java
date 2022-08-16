package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.TaskParticipant;
import cn.atsoft.dasheng.form.model.params.TaskParticipantParam;
import cn.atsoft.dasheng.form.model.result.TaskParticipantResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-08-16
 */
public interface TaskParticipantMapper extends BaseMapper<TaskParticipant> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-08-16
     */
    List<TaskParticipantResult> customList(@Param("paramCondition") TaskParticipantParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-08-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TaskParticipantParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-08-16
     */
    Page<TaskParticipantResult> customPageList(@Param("page") Page page, @Param("paramCondition") TaskParticipantParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-08-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TaskParticipantParam paramCondition);

}

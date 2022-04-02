package cn.atsoft.dasheng.task.mapper;

import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 等待任务表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-01
 */
public interface AsynTaskMapper extends BaseMapper<AsynTask> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-01
     */
    List<AsynTaskResult> customList(@Param("paramCondition") AsynTaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AsynTaskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-01
     */
    Page<AsynTaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") AsynTaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AsynTaskParam paramCondition);

}

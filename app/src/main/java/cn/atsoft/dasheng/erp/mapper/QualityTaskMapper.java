package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检任务 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-11-16
 */
public interface QualityTaskMapper extends BaseMapper<QualityTask> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-11-16
     */
    List<QualityTaskResult> customList(@Param("paramCondition") QualityTaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-11-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityTaskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-11-16
     */
    Page<QualityTaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityTaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-11-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityTaskParam paramCondition);

}

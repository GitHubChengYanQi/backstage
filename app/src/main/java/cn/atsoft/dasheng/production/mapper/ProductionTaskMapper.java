package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产任务 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
public interface ProductionTaskMapper extends BaseMapper<ProductionTask> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    List<ProductionTaskResult> customList(@Param("paramCondition") ProductionTaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionTaskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    Page<ProductionTaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionTaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionTaskParam paramCondition);

}

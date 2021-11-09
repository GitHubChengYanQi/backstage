package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.WorkingProcedure;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
public interface WorkingProcedureMapper extends BaseMapper<WorkingProcedure> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-29
     */
    List<WorkingProcedureResult> customList(@Param("paramCondition") WorkingProcedureParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WorkingProcedureParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-29
     */
    Page<WorkingProcedureResult> customPageList(@Param("page") Page page, @Param("paramCondition") WorkingProcedureParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WorkingProcedureParam paramCondition);

}

package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.WorkingProcedureClass;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序分类表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
public interface WorkingPorcedureClassMapper extends BaseMapper<WorkingProcedureClass> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-29
     */
    List<WorkingProcedureClassResult> customList(@Param("paramCondition") WorkingProcedureClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WorkingProcedureClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-29
     */
    Page<WorkingProcedureClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") WorkingProcedureClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WorkingProcedureClassParam paramCondition);

}

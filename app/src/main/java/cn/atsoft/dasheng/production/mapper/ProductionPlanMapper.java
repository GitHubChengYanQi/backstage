package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产计划主表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
public interface ProductionPlanMapper extends BaseMapper<ProductionPlan> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-02-25
     */
    List<ProductionPlanResult> customList(@Param("paramCondition") ProductionPlanParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-02-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPlanParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-02-25
     */
    Page<ProductionPlanResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPlanParam paramCondition, @Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-02-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPlanParam paramCondition);

}

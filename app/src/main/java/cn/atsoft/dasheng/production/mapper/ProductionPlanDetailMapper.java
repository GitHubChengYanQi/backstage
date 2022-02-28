package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产计划子表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
public interface ProductionPlanDetailMapper extends BaseMapper<ProductionPlanDetail> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-02-25
     */
    List<ProductionPlanDetailResult> customList(@Param("paramCondition") ProductionPlanDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-02-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPlanDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-02-25
     */
    Page<ProductionPlanDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPlanDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-02-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPlanDetailParam paramCondition);

}

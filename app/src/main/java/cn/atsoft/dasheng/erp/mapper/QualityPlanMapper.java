package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检方案 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
public interface QualityPlanMapper extends BaseMapper<QualityPlan> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<QualityPlanResult> customList(@Param("paramCondition") QualityPlanParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityPlanParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    Page<QualityPlanResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityPlanParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityPlanParam paramCondition);

}

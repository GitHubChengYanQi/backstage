package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购计划主表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface ProcurementPlanMapper extends BaseMapper<ProcurementPlan> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-21
     */
    List<ProcurementPlanResult> customList(@Param("paramCondition") ProcurementPlanParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProcurementPlanParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-21
     */
    Page<ProcurementPlanResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProcurementPlanParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProcurementPlanParam paramCondition);

}

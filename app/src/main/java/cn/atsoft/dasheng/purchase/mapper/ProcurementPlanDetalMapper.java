package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购计划单子表  整合数据后的 子表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface ProcurementPlanDetalMapper extends BaseMapper<ProcurementPlanDetal> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-21
     */
    List<ProcurementPlanDetalResult> customList(@Param("paramCondition") ProcurementPlanDetalParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProcurementPlanDetalParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-21
     */
    Page<ProcurementPlanDetalResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProcurementPlanDetalParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProcurementPlanDetalParam paramCondition);

}

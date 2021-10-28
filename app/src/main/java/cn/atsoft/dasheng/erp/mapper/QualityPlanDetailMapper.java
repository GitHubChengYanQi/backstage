package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检方案详情 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
public interface QualityPlanDetailMapper extends BaseMapper<QualityPlanDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<QualityPlanDetailResult> customList(@Param("paramCondition") QualityPlanDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityPlanDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    Page<QualityPlanDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityPlanDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityPlanDetailParam paramCondition);

}

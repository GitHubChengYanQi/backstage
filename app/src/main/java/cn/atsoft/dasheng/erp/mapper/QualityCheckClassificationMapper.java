package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityCheckClassification;
import cn.atsoft.dasheng.erp.model.params.QualityCheckClassificationParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检分类表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
public interface QualityCheckClassificationMapper extends BaseMapper<QualityCheckClassification> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-27
     */
    List<QualityCheckClassificationResult> customList(@Param("paramCondition") QualityCheckClassificationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityCheckClassificationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-27
     */
    Page<QualityCheckClassificationResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityCheckClassificationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityCheckClassificationParam paramCondition);

}

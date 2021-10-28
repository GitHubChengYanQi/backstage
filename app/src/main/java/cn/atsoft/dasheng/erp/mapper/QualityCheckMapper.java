package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.QualityCheck;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质检表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
public interface QualityCheckMapper extends BaseMapper<QualityCheck> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-27
     */
    List<QualityCheckResult> customList(@Param("paramCondition") QualityCheckParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QualityCheckParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-27
     */
    Page<QualityCheckResult> customPageList(@Param("page") Page page, @Param("paramCondition") QualityCheckParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QualityCheckParam paramCondition);

}

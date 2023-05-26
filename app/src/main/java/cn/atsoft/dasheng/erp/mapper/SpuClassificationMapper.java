package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.params.SpuClassificationParam;
import cn.atsoft.dasheng.erp.model.result.SkuCountByClassIdResult;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SPU分类 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface SpuClassificationMapper extends BaseMapper<SpuClassification> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<SpuClassificationResult> customList(@Param("paramCondition") SpuClassificationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SpuClassificationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<SpuClassificationResult> customPageList(@Param("page") Page page, @Param("paramCondition") SpuClassificationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SpuClassificationParam paramCondition);
    List<SkuCountByClassIdResult> getSkuCountByClassIds(@Param("classIds") List<Long> classIds,@Param("tenantId") Long tenantId);

}

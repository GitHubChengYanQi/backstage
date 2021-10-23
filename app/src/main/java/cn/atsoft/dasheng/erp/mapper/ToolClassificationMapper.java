package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ToolClassification;
import cn.atsoft.dasheng.erp.model.params.ToolClassificationParam;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工具分类表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
public interface ToolClassificationMapper extends BaseMapper<ToolClassification> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-23
     */
    List<ToolClassificationResult> customList(@Param("paramCondition") ToolClassificationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ToolClassificationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-23
     */
    Page<ToolClassificationResult> customPageList(@Param("page") Page page, @Param("paramCondition") ToolClassificationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ToolClassificationParam paramCondition);

}

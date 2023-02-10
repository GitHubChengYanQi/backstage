package cn.atsoft.dasheng.mapper;

//import cn.atsoft.dasheng.erp.entity.Traceability;
//import cn.atsoft.dasheng.erp.model.params.TraceabilityParam;
//import cn.atsoft.dasheng.erp.model.result.TraceabilityResult;
import cn.atsoft.dasheng.entity.RestTraceability;
import cn.atsoft.dasheng.model.params.RestTraceabilityParam;
import cn.atsoft.dasheng.model.result.RestTraceabilityResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实物表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
public interface RestTraceabilityMapper extends BaseMapper<RestTraceability> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-11-01
     */
    List<RestTraceabilityResult> customList(@Param("paramCondition") RestTraceabilityParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-11-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestTraceabilityParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<RestTraceabilityResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestTraceabilityParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestTraceabilityParam paramCondition);


    /**
     * 库存实物
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<RestTraceabilityResult> stockTraceabilityList(@Param("page") Page page, @Param("paramCondition") RestTraceabilityParam paramCondition);

}

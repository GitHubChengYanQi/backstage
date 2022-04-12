package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 异常单据 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
public interface AnomalyMapper extends BaseMapper<Anomaly> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-12
     */
    List<AnomalyResult> customList(@Param("paramCondition") AnomalyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-12
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AnomalyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-12
     */
    Page<AnomalyResult> customPageList(@Param("page") Page page, @Param("paramCondition") AnomalyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AnomalyParam paramCondition);

}

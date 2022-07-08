package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.MaintenanceCycle;
import cn.atsoft.dasheng.erp.model.params.MaintenanceCycleParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceCycleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料维护周期 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-08
 */
public interface MaintenanceCycleMapper extends BaseMapper<MaintenanceCycle> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    List<MaintenanceCycleResult> customList(@Param("paramCondition") MaintenanceCycleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaintenanceCycleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    Page<MaintenanceCycleResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaintenanceCycleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaintenanceCycleParam paramCondition);

}

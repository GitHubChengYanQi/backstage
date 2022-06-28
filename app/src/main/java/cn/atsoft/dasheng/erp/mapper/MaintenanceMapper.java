package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 养护申请主表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
public interface MaintenanceMapper extends BaseMapper<Maintenance> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<MaintenanceResult> customList(@Param("paramCondition") MaintenanceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaintenanceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    Page<MaintenanceResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaintenanceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaintenanceParam paramCondition);

}

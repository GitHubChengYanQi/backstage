package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.MaintenanceLog;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 养护记录 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
public interface MaintenanceLogMapper extends BaseMapper<MaintenanceLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<MaintenanceLogResult> customList(@Param("paramCondition") MaintenanceLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaintenanceLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<MaintenanceLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaintenanceLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaintenanceLogParam paramCondition);

}

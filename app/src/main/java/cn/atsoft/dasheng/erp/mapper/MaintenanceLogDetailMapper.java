package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.MaintenanceLogDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
public interface MaintenanceLogDetailMapper extends BaseMapper<MaintenanceLogDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<MaintenanceLogDetailResult> customList(@Param("paramCondition") MaintenanceLogDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaintenanceLogDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<MaintenanceLogDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaintenanceLogDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaintenanceLogDetailParam paramCondition);

}

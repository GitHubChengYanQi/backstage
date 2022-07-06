package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 养护申请子表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
public interface MaintenanceDetailMapper extends BaseMapper<MaintenanceDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<MaintenanceDetailResult> customList(@Param("paramCondition") MaintenanceDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MaintenanceDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    Page<MaintenanceDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") MaintenanceDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MaintenanceDetailParam paramCondition);

}

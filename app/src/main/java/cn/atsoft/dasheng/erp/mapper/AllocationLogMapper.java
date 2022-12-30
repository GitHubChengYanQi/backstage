package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AllocationLog;
import cn.atsoft.dasheng.erp.model.params.AllocationLogParam;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogResult;
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
public interface AllocationLogMapper extends BaseMapper<AllocationLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<AllocationLogResult> customList(@Param("paramCondition") AllocationLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AllocationLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<AllocationLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") AllocationLogParam paramCondition);
    List<AllocationLogResult> countByCreateUser( @Param("paramCondition") DataStatisticsViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AllocationLogParam paramCondition);

}

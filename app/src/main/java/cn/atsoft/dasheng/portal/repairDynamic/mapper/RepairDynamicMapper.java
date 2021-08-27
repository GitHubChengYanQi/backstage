package cn.atsoft.dasheng.portal.repairDynamic.mapper;

import cn.atsoft.dasheng.portal.repairDynamic.entity.RepairDynamic;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.model.result.RepairDynamicResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 售后动态表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
public interface RepairDynamicMapper extends BaseMapper<RepairDynamic> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<RepairDynamicResult> customList(@Param("paramCondition") RepairDynamicParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RepairDynamicParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<RepairDynamicResult> customPageList(@Param("page") Page page, @Param("paramCondition") RepairDynamicParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RepairDynamicParam paramCondition);

}

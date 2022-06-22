package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盘点任务主表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<InventoryResult> customList(@Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    Page<InventoryResult> customPageList(@Param("page") Page page, @Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InventoryParam paramCondition);

}

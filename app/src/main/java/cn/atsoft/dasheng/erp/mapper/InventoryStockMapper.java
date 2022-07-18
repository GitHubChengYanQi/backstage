package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.model.params.InventoryStockParam;
import cn.atsoft.dasheng.erp.model.result.InventoryStockResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存盘点处理 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
public interface InventoryStockMapper extends BaseMapper<InventoryStock> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-07-15
     */
    List<InventoryStockResult> customList(@Param("paramCondition") InventoryStockParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InventoryStockParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-07-15
     */
    Page<InventoryStockResult> customPageList(@Param("page") Page page, @Param("paramCondition") InventoryStockParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InventoryStockParam paramCondition);

}

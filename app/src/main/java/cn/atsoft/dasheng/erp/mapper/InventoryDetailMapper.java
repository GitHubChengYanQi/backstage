package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 盘点任务详情 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
public interface InventoryDetailMapper extends BaseMapper<InventoryDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<InventoryDetailResult> customList(@Param("paramCondition") InventoryDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InventoryDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    Page<InventoryDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") InventoryDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InventoryDetailParam paramCondition);



    List<Long> getSkuIds(@Param("classIds") List<Long> classIds);
}

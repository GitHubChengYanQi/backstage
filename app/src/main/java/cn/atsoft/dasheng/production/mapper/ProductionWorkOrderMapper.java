package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
public interface ProductionWorkOrderMapper extends BaseMapper<ProductionWorkOrder> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionWorkOrderResult> customList(@Param("paramCondition") ProductionWorkOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionWorkOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<ProductionWorkOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionWorkOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionWorkOrderParam paramCondition);

}

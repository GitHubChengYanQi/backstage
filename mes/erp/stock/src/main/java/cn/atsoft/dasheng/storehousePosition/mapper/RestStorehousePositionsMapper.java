package cn.atsoft.dasheng.storehousePosition.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
//import cn.atsoft.dasheng.erp.entity.StorehousePositions;
//import cn.atsoft.dasheng.erp.model.params.RestStorehousePositionsParam;
//import cn.atsoft.dasheng.erp.model.result.RestStorehousePositionsResult;
import cn.atsoft.dasheng.storehousePosition.entity.RestStorehousePositions;
//import cn.atsoft.dasheng.storehousePosition.model.params.RestRestStorehousePositionsParam;
import cn.atsoft.dasheng.storehousePosition.model.params.RestStorehousePositionsParam;
//import cn.atsoft.dasheng.storehousePosition.model.result.RestRestStorehousePositionsResult;
import cn.atsoft.dasheng.storehousePosition.model.result.RestStorehousePositionsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库库位表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface RestStorehousePositionsMapper extends BaseMapper<RestStorehousePositions> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<RestStorehousePositionsResult> customList(@Param("paramCondition") RestStorehousePositionsParam paramCondition, @Param("dataScope")DataScope dataScope);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestStorehousePositionsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<RestStorehousePositionsResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestStorehousePositionsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestStorehousePositionsParam paramCondition);

}

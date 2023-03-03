package cn.atsoft.dasheng.storehousePositionBind.mapper;

import cn.atsoft.dasheng.storehousePositionBind.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.storehousePositionBind.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.storehousePositionBind.model.result.StorehousePositionsBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位绑定物料表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
public interface RestStorehousePositionsBindMapper extends BaseMapper<StorehousePositionsBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-01-20
     */
    List<StorehousePositionsBindResult> customList(@Param("paramCondition") StorehousePositionsBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-01-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StorehousePositionsBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-01-20
     */
    Page<StorehousePositionsBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-01-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsBindParam paramCondition);

}

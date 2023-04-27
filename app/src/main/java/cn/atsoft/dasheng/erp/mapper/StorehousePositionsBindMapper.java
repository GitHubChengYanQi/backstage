package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface StorehousePositionsBindMapper extends BaseMapper<StorehousePositionsBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-01-20
     */
    List<StorehousePositionsBindResult> customList(@Param("paramCondition") StorehousePositionsBindParam paramCondition,@Param("dataScope") DataScope dataScope);

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

package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface StorehousePositionsMapper extends BaseMapper<StorehousePositions> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<StorehousePositionsResult> customList(@Param("paramCondition") StorehousePositionsParam paramCondition, @Param("dataScope")DataScope dataScope);
    List<StorehousePositionsResult> getLowestLevelPositions( @Param("pid")Long pid,@Param("tenantId")Long tenantId);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StorehousePositionsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<StorehousePositionsResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsParam paramCondition);

}

package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工位表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface ProductionStationMapper extends BaseMapper<ProductionStation> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<ProductionStationResult> customList(@Param("paramCondition") ProductionStationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionStationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<ProductionStationResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionStationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionStationParam paramCondition);

}

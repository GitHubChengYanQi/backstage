package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionStationBind;
import cn.atsoft.dasheng.production.model.params.ProductionStationBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工位绑定表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
public interface ProductionStationBindMapper extends BaseMapper<ProductionStationBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<ProductionStationBindResult> customList(@Param("paramCondition") ProductionStationBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionStationBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    Page<ProductionStationBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionStationBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionStationBindParam paramCondition);

}

package cn.atsoft.dasheng.production.card.mapper;

import cn.atsoft.dasheng.production.card.entity.ProductionCard;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产卡片 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
public interface RestProductionCardMapper extends BaseMapper<ProductionCard> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    List<ProductionCardResult> customList(@Param("paramCondition") ProductionCardParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionCardParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    Page<ProductionCardResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionCardParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionCardParam paramCondition);

}

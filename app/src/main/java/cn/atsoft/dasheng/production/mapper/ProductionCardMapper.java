package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产卡片 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
public interface ProductionCardMapper extends BaseMapper<ProductionCard> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionCardResult> customList(@Param("paramCondition") ProductionCardParam paramCondition);


    List<ProductionCardResult> grupByProductionPlan(@Param("paramCondition") ProductionCardParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionCardParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<ProductionCardResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionCardParam paramCondition, @Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionCardParam paramCondition);

}

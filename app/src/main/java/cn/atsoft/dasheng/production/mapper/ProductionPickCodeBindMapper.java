package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionPickCodeBind;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-29
 */
public interface ProductionPickCodeBindMapper extends BaseMapper<ProductionPickCodeBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    List<ProductionPickCodeBindResult> customList(@Param("paramCondition") ProductionPickCodeBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPickCodeBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    Page<ProductionPickCodeBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPickCodeBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPickCodeBindParam paramCondition);

}

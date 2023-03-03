package cn.atsoft.dasheng.goods.brand.mapper;


import cn.atsoft.dasheng.goods.brand.entity.RestSkuBrandBind;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestSkuBrandBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2022-01-18
 */
public interface RestSkuBrandBindMapper extends BaseMapper<RestSkuBrandBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<RestSkuBrandBindResult> customList(@Param("paramCondition") RestSkuBrandBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestSkuBrandBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    Page<RestSkuBrandBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestSkuBrandBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestSkuBrandBindParam paramCondition);

}

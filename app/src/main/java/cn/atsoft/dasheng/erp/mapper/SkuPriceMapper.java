package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SkuPrice;
import cn.atsoft.dasheng.erp.model.params.SkuPriceParam;
import cn.atsoft.dasheng.erp.model.result.SkuPriceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品价格设置表 Mapper 接口
 * </p>
 *
 * @author sjl
 * @since 2023-01-09
 */
public interface SkuPriceMapper extends BaseMapper<SkuPrice> {

    /**
     * 获取列表
     *
     * @author sjl
     * @Date 2023-01-09
     */
    List<SkuPriceResult> customList(@Param("paramCondition") SkuPriceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author sjl
     * @Date 2023-01-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuPriceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author sjl
     * @Date 2023-01-09
     */
    Page<SkuPriceResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuPriceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author sjl
     * @Date 2023-01-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuPriceParam paramCondition);

}

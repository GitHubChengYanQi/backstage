package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.model.params.SkuValuesParam;
import cn.atsoft.dasheng.erp.model.result.SkuValuesResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku详情表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
public interface SkuValuesMapper extends BaseMapper<SkuValues> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<SkuValuesResult> customList(@Param("paramCondition") SkuValuesParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuValuesParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<SkuValuesResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuValuesParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuValuesParam paramCondition);

}

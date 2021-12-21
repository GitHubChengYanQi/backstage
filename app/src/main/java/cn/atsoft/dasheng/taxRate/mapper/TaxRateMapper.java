package cn.atsoft.dasheng.taxRate.mapper;

import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.model.params.TaxRateParam;
import cn.atsoft.dasheng.taxRate.model.result.TaxRateResult;
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
 * @author 
 * @since 2021-12-21
 */
public interface TaxRateMapper extends BaseMapper<TaxRate> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-12-21
     */
    List<TaxRateResult> customList(@Param("paramCondition") TaxRateParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-12-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TaxRateParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-12-21
     */
    Page<TaxRateResult> customPageList(@Param("page") Page page, @Param("paramCondition") TaxRateParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-12-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TaxRateParam paramCondition);

}

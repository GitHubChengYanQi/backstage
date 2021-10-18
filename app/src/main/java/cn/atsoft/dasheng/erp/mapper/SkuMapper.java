package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku表	 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
public interface SkuMapper extends BaseMapper<Sku> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<SkuResult> customList(@Param("paramCondition") SkuParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<SkuResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuParam paramCondition);

}

package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.params.SkuBrandBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuBrandBindResult;
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
 * @since 2022-01-18
 */
public interface SkuBrandBindMapper extends BaseMapper<SkuBrandBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<SkuBrandBindResult> customList(@Param("paramCondition") SkuBrandBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SkuBrandBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    Page<SkuBrandBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") SkuBrandBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SkuBrandBindParam paramCondition);

}

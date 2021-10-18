package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品分类表 Mapper 接口
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获取列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<CategoryResult> customList(@Param("paramCondition") CategoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CategoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Page<CategoryResult> customPageList(@Param("page") Page page, @Param("paramCondition") CategoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CategoryParam paramCondition);

}

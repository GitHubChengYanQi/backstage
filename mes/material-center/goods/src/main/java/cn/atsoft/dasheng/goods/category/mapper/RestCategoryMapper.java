package cn.atsoft.dasheng.goods.category.mapper;


import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.params.RestCategoryParam;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SPU分类 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface RestCategoryMapper extends BaseMapper<RestCategory> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<RestCategoryResult> customList(@Param("paramCondition") RestCategoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestCategoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<RestCategoryResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestCategoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestCategoryParam paramCondition);

}

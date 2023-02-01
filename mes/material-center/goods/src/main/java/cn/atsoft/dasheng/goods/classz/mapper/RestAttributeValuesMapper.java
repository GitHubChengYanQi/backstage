package cn.atsoft.dasheng.goods.classz.mapper;


import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeValuesResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品属性数据表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface RestAttributeValuesMapper extends BaseMapper<RestAttributeValues> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<RestAttributeValuesResult> customList(@Param("paramCondition") RestAttributeValuesParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestAttributeValuesParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<RestAttributeValuesResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestAttributeValuesParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestAttributeValuesParam paramCondition);

}

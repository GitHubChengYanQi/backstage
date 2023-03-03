package cn.atsoft.dasheng.goods.classz.mapper;


import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RestClassMapper extends BaseMapper<RestClass> {

    /**
     * 获取列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<RestClassResult> customList(@Param("paramCondition") RestClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Page<RestClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestClassParam paramCondition);

}

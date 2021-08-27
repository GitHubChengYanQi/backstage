package cn.atsoft.dasheng.portal.navigationDifference.mapper;

import cn.atsoft.dasheng.portal.navigationDifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationDifference.model.params.NavigationDifferenceParam;
import cn.atsoft.dasheng.portal.navigationDifference.model.result.NavigationDifferenceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导航分类 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface NavigationDifferenceMapper extends BaseMapper<NavigationDifference> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<NavigationDifferenceResult> customList(@Param("paramCondition") NavigationDifferenceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") NavigationDifferenceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<NavigationDifferenceResult> customPageList(@Param("page") Page page, @Param("paramCondition") NavigationDifferenceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") NavigationDifferenceParam paramCondition);

}

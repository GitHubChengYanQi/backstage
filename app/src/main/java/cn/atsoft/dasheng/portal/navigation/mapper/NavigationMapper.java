package cn.atsoft.dasheng.portal.navigation.mapper;

import cn.atsoft.dasheng.portal.navigation.entity.Navigation;
import cn.atsoft.dasheng.portal.navigation.model.params.NavigationParam;
import cn.atsoft.dasheng.portal.navigation.model.result.NavigationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导航表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface NavigationMapper extends BaseMapper<Navigation> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<NavigationResult> customList(@Param("paramCondition") NavigationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") NavigationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<NavigationResult> customPageList(@Param("page") Page page, @Param("paramCondition") NavigationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") NavigationParam paramCondition);

}

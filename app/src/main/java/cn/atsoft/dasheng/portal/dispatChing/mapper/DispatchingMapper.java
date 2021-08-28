package cn.atsoft.dasheng.portal.dispatChing.mapper;

import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatChing.model.result.DispatchingResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 派工表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-23
 */
public interface DispatchingMapper extends BaseMapper<Dispatching> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-23
     */
    List<DispatchingResult> customList(@Param("paramCondition") DispatchingParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DispatchingParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-23
     */
    Page<DispatchingResult> customPageList(@Param("page") Page page, @Param("paramCondition") DispatchingParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DispatchingParam paramCondition);

}

package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工艺路线列表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
public interface ProcessRouteMapper extends BaseMapper<ProcessRoute> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<ProcessRouteResult> customList(@Param("paramCondition") ProcessRouteParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProcessRouteParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    Page<ProcessRouteResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProcessRouteParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProcessRouteParam paramCondition);

}

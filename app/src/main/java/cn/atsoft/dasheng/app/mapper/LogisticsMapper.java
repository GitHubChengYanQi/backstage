package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Logistics;
import cn.atsoft.dasheng.app.model.params.LogisticsParam;
import cn.atsoft.dasheng.app.model.result.LogisticsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface LogisticsMapper extends BaseMapper<Logistics> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<LogisticsResult> customList(@Param("paramCondition") LogisticsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") LogisticsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<LogisticsResult> customPageList(@Param("page") Page page, @Param("paramCondition") LogisticsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") LogisticsParam paramCondition);

}

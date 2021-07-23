package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.PartsList;
import cn.atsoft.dasheng.app.model.params.PartsListParam;
import cn.atsoft.dasheng.app.model.result.PartsListResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 零件表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
public interface PartsListMapper extends BaseMapper<PartsList> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<PartsListResult> customList(@Param("paramCondition") PartsListParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PartsListParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<PartsListResult> customPageList(@Param("page") Page page, @Param("paramCondition") PartsListParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PartsListParam paramCondition);

}

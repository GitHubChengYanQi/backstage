package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Source;
import cn.atsoft.dasheng.app.model.params.SourceParam;
import cn.atsoft.dasheng.app.model.result.SourceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 来源表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-19
 */
public interface SourceMapper extends BaseMapper<Source> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-19
     */
    List<SourceResult> customList(@Param("paramCondition") SourceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SourceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-19
     */
    Page<SourceResult> customPageList(@Param("page") Page page, @Param("paramCondition") SourceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SourceParam paramCondition);

}

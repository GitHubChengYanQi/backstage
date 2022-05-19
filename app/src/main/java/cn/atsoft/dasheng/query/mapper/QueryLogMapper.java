package cn.atsoft.dasheng.query.mapper;

import cn.atsoft.dasheng.query.entity.QueryLog;
import cn.atsoft.dasheng.query.model.params.QueryLogParam;
import cn.atsoft.dasheng.query.model.result.QueryLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 搜索查询记录 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-05-19
 */
public interface QueryLogMapper extends BaseMapper<QueryLog> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-05-19
     */
    List<QueryLogResult> customList(@Param("paramCondition") QueryLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-05-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QueryLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-05-19
     */
    Page<QueryLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") QueryLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-05-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QueryLogParam paramCondition);

}

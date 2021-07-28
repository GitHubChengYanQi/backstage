package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Track;
import cn.atsoft.dasheng.app.model.params.TrackParam;
import cn.atsoft.dasheng.app.model.result.TrackResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报价表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
public interface TrackMapper extends BaseMapper<Track> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<TrackResult> customList(@Param("paramCondition") TrackParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TrackParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Page<TrackResult> customPageList(@Param("page") Page page, @Param("paramCondition") TrackParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TrackParam paramCondition);

}

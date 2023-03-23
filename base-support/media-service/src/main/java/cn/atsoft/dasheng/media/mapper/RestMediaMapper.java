package cn.atsoft.dasheng.media.mapper;

import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.model.params.MediaParam;
import cn.atsoft.dasheng.media.model.result.MediaResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
public interface RestMediaMapper extends BaseMapper<Media> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    List<MediaResult> customList(@Param("paramCondition") MediaParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MediaParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    Page<MediaResult> customPageList(@Param("page") Page page, @Param("paramCondition") MediaParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MediaParam paramCondition);

}

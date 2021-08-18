package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusinessTrack;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机跟踪表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessTrackMapper extends BaseMapper<CrmBusinessTrack> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessTrackResult> customList(@Param("paramCondition") CrmBusinessTrackParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessTrackParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<CrmBusinessTrackResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessTrackParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessTrackParam paramCondition);

}

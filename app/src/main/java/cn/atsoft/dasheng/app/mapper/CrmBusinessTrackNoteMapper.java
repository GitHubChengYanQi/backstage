package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusinessTrackNote;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackNoteParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackNoteResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机跟踪备注 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessTrackNoteMapper extends BaseMapper<CrmBusinessTrackNote> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessTrackNoteResult> customList(@Param("paramCondition") CrmBusinessTrackNoteParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessTrackNoteParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<CrmBusinessTrackNoteResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessTrackNoteParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessTrackNoteParam paramCondition);

}

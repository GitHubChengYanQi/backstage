package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.model.params.AnnouncementsParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 注意事项 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
public interface AnnouncementsMapper extends BaseMapper<Announcements> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-05-27
     */
    List<AnnouncementsResult> customList(@Param("paramCondition") AnnouncementsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-05-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AnnouncementsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-05-27
     */
    Page<AnnouncementsResult> customPageList(@Param("page") Page page, @Param("paramCondition") AnnouncementsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-05-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AnnouncementsParam paramCondition);

}

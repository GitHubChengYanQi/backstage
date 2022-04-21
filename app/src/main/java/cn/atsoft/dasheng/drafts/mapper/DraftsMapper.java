package cn.atsoft.dasheng.drafts.mapper;

import cn.atsoft.dasheng.drafts.entity.Drafts;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import cn.atsoft.dasheng.drafts.model.result.DraftsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 草稿箱 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-21
 */
public interface DraftsMapper extends BaseMapper<Drafts> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    List<DraftsResult> customList(@Param("paramCondition") DraftsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DraftsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    Page<DraftsResult> customPageList(@Param("page") Page page, @Param("paramCondition") DraftsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DraftsParam paramCondition);

}

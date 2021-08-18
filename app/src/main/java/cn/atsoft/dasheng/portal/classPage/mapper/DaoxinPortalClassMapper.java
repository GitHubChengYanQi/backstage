package cn.atsoft.dasheng.protal.classPage.mapper;

import cn.atsoft.dasheng.protal.classPage.entity.DaoxinPortalClass;
import cn.atsoft.dasheng.protal.classPage.model.params.DaoxinPortalClassParam;
import cn.atsoft.dasheng.protal.classPage.model.result.DaoxinPortalClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类导航 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface DaoxinPortalClassMapper extends BaseMapper<DaoxinPortalClass> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<DaoxinPortalClassResult> customList(@Param("paramCondition") DaoxinPortalClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DaoxinPortalClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<DaoxinPortalClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") DaoxinPortalClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DaoxinPortalClassParam paramCondition);

}

package cn.atsoft.dasheng.app.cloude.portal.mapper;

import cn.atsoft.dasheng.app.cloude.portal.entity.PortalBanner;
import cn.atsoft.dasheng.app.cloude.portal.model.params.PortalBannerParam;
import cn.atsoft.dasheng.app.cloude.portal.model.result.PortalBannerResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 轮播图 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-17
 */
public interface PortalBannerMapper extends BaseMapper<PortalBanner> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-17
     */
    List<PortalBannerResult> customList(@Param("paramCondition") PortalBannerParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PortalBannerParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-17
     */
    Page<PortalBannerResult> customPageList(@Param("page") Page page, @Param("paramCondition") PortalBannerParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PortalBannerParam paramCondition);

}

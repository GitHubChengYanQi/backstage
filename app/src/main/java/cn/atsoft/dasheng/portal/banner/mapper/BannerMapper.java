package  cn.atsoft.dasheng.portal.banner.mapper;


import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
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
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-17
     */
    List<BannerResult> customList(@Param("paramCondition") BannerParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BannerParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-17
     */
    Page<BannerResult> customPageList(@Param("page") Page page, @Param("paramCondition") BannerParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BannerParam paramCondition);

}

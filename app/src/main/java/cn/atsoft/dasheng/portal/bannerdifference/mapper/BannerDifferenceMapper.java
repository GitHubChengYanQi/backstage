package cn.atsoft.dasheng.portal.bannerdifference.mapper;

import cn.atsoft.dasheng.portal.bannerdifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerdifference.model.params.BannerDifferenceParam;
import cn.atsoft.dasheng.portal.bannerdifference.model.result.BannerDifferenceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 轮播图分类 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
public interface BannerDifferenceMapper extends BaseMapper<BannerDifference> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<BannerDifferenceResult> customList(@Param("paramCondition") BannerDifferenceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BannerDifferenceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<BannerDifferenceResult> customPageList(@Param("page") Page page, @Param("paramCondition") BannerDifferenceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BannerDifferenceParam paramCondition);

}

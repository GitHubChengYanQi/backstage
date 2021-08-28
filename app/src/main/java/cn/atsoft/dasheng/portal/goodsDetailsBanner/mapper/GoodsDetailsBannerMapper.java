package cn.atsoft.dasheng.portal.goodsDetailsBanner.mapper;

import cn.atsoft.dasheng.portal.goodsDetailsBanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.model.result.GoodsDetailsBannerResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品轮播图 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsDetailsBannerMapper extends BaseMapper<GoodsDetailsBanner> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsDetailsBannerResult> customList(@Param("paramCondition") GoodsDetailsBannerParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") GoodsDetailsBannerParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<GoodsDetailsBannerResult> customPageList(@Param("page") Page page, @Param("paramCondition") GoodsDetailsBannerParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") GoodsDetailsBannerParam paramCondition);

}

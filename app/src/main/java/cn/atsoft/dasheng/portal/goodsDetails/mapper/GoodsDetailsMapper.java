package cn.atsoft.dasheng.portal.goodsDetails.mapper;

import cn.atsoft.dasheng.portal.goodsDetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsDetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsDetails.model.result.GoodsDetailsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页商品详情 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsDetailsMapper extends BaseMapper<GoodsDetails> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsDetailsResult> customList(@Param("paramCondition") GoodsDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") GoodsDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<GoodsDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") GoodsDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") GoodsDetailsParam paramCondition);

}

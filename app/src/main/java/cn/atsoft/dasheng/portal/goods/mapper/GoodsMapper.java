package cn.atsoft.dasheng.portal.goods.mapper;

import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页商品 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<GoodsResult> customList(@Param("paramCondition") GoodsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") GoodsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<GoodsResult> customPageList(@Param("page") Page page, @Param("paramCondition") GoodsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") GoodsParam paramCondition);

}

package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ShopCart;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.ShopCartResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
public interface ShopCartMapper extends BaseMapper<ShopCart> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-06-06
     */
    List<ShopCartResult> customList(@Param("paramCondition") ShopCartParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-06-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ShopCartParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-06-06
     */
    Page<ShopCartResult> customPageList(@Param("page") Page page, @Param("paramCondition") ShopCartParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-06-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ShopCartParam paramCondition);

}

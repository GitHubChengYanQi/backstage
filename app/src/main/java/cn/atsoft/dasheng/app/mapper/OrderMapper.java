package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Order;
import cn.atsoft.dasheng.app.model.params.OrderParam;
import cn.atsoft.dasheng.app.model.result.OrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 获取列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    List<OrderResult> customList(@Param("paramCondition") OrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    Page<OrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrderParam paramCondition);

}

package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.OrderDetails;
import cn.atsoft.dasheng.app.model.params.OrderDetailsParam;
import cn.atsoft.dasheng.app.model.result.OrderDetailsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<OrderDetailsResult> customList(@Param("paramCondition") OrderDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrderDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<OrderDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrderDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrderDetailsParam paramCondition);

}

package cn.atsoft.dasheng.inStock.mapper;


import cn.atsoft.dasheng.inStock.entity.RestInstockOrder;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderParam;
import cn.atsoft.dasheng.inStock.model.result.OrderResult;
import cn.atsoft.dasheng.inStock.model.result.RestInstockOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface RestInstockOrderMapper extends BaseMapper<RestInstockOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<RestInstockOrderResult> customList(@Param("paramCondition") RestInstockOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestInstockOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<RestInstockOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestInstockOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestInstockOrderParam paramCondition);
    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<OrderResult> orderResult(@Param("page") Page page, @Param("paramCondition") RestInstockOrderParam paramCondition);

}

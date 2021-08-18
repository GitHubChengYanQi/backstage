package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ErpOrder;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2021-07-26
 */
public interface ErpOrderMapper extends BaseMapper<ErpOrder> {

    /**
     * 获取列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<ErpOrderResult> customList(@Param("paramCondition") ErpOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ErpOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    Page<ErpOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") ErpOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ErpOrderParam paramCondition);

}

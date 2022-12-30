package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
public interface OutstockOrderMapper extends BaseMapper<OutstockOrder> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    List<OutstockOrderResult> customList(@Param("paramCondition") OutstockOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OutstockOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    Page<OutstockOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutstockOrderParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutstockOrderParam paramCondition);

    Page<StockView> groupByUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> groupByUserList(@Param("paramCondition") DataStatisticsViewParam paramCondition);


}

package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库清单 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
public interface OutstockListingMapper extends BaseMapper<OutstockListing> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-09-15
     */
    List<OutstockListingResult> customList(@Param("paramCondition") OutstockListingParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-09-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OutstockListingParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-09-15
     */
    Page<OutstockListingResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutstockListingParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-09-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutstockListingParam paramCondition);
    List<StockView> groupByUser(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> groupByMonth(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> count(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> outBySpuClass(@Param("paramCondition") DataStatisticsViewParam paramCondition);

}

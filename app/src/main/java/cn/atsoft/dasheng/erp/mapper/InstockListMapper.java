package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库清单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface InstockListMapper extends BaseMapper<InstockList> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<InstockListResult> customList(@Param("paramCondition") InstockListParam paramCondition);


    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockListParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<InstockListResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockListParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockListParam paramCondition);

    List<StockView> groupBySpuClass(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> groupByInstockType(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> groupByStorehouse(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> groupByCustomerSku(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> groupByCustomerNum(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);

}

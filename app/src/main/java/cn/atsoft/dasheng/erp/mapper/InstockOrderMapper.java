package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface InstockOrderMapper extends BaseMapper<InstockOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<InstockOrderResult> customList(@Param("paramCondition") InstockOrderParam paramCondition);


    List<StockView> countOrderByType(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> countOrderByStatus(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> countOrderByUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> sumOrderByUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView.SkuAndNumber> instockDetails(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> instockCustomer(@Param("paramCondition") DataStatisticsViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<InstockOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockOrderParam paramCondition);

}

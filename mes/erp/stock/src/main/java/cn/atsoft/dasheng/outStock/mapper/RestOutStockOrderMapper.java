package cn.atsoft.dasheng.outStock.mapper;


import cn.atsoft.dasheng.outStock.entity.RestOutStockOrder;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface RestOutStockOrderMapper extends BaseMapper<RestOutStockOrder> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<RestOutStockOrderResult> customList(@Param("paramCondition") RestOutStockOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestOutStockOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
//    Page<StockView> outstockUserView(@Param("page") Page page, @Param("paramCondition") DataStatisticsViewParam paramCondition);
//    List<StockView> outstockView(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<RestOutStockOrder> customPageList(@Param("page") Page page, @Param("paramCondition") RestOutStockOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestOutStockOrderParam paramCondition);
    List<Long> idsList(@Param("paramCondition") RestOutStockOrderParam paramCondition);

//    List<StockView> orderCountByType(@Param("paramCondition") DataStatisticsViewParam paramCondition);
//    List<StockView> orderCountByStatus(@Param("paramCondition") DataStatisticsViewParam paramCondition);
//    Page<StockView> orderCountByCreateUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
//    Page<StockView> orderDetailCountByCreateUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);

}

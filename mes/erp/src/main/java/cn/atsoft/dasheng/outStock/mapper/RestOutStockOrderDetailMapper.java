package cn.atsoft.dasheng.outStock.mapper;

import cn.atsoft.dasheng.outStock.entity.RestOutStockOrderDetail;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderDetailParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockOrderDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单详情表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface RestOutStockOrderDetailMapper extends BaseMapper<RestOutStockOrderDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<RestOutStockOrderDetailResult> customList(@Param("paramCondition") RestOutStockOrderDetailParam paramCondition);
    Page<RestOutStockOrderDetailResult> pickListsDetailList(@Param("page") Page page, @Param("paramCondition") RestOutStockOrderDetailParam paramCondition);
    List<Long> getSkuIdsByPickLists(@Param("pickListsId") Long pickListsId);

    List<RestOutStockOrderDetailResult> customList2(@Param("paramCondition") RestOutStockOrderDetailParam paramCondition);

    
    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestOutStockOrderDetailParam paramCondition);

//    List<StockView> userSkuAndNumbers(@Param("paramCondition")DataStatisticsViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<RestOutStockOrderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestOutStockOrderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestOutStockOrderDetailParam paramCondition);

}

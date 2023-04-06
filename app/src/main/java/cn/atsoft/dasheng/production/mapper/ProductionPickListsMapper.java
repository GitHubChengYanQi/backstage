package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
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
public interface ProductionPickListsMapper extends BaseMapper<ProductionPickLists> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<ProductionPickListsResult> customList(@Param("paramCondition") ProductionPickListsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionPickListsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<StockView> outstockUserView(@Param("page") Page page, @Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> outstockView(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<ProductionPickListsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionPickListsParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionPickListsParam paramCondition);
    List<Long> idsList(@Param("paramCondition") ProductionPickListsParam paramCondition);

    List<StockView> orderCountByType(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockView> orderCountByStatus(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> orderCountByCreateUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> orderDetailCountByCreateUser(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);

}

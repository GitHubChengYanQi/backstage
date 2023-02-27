package cn.atsoft.dasheng.stockDetail.mapper;

//import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
//import cn.atsoft.dasheng.app.entity.StockDetails;
//import cn.atsoft.dasheng.app.model.params.RestStockDetailsParam;
//import cn.atsoft.dasheng.app.model.request.StockDetailView;
//import cn.atsoft.dasheng.app.model.result.RestStockDetailsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
//import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 仓库物品明细表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
public interface RestStockDetailsMapper extends BaseMapper<RestStockDetails> {

//    /**
//     * 获取列表
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    List<RestStockDetailsResult> customList(@Param("paramCondition") RestStockDetailsParam paramCondition);
//
//
//
////    List<StockDetailExcel> stockDetailExcelExport();
//
//    /**
//     * 获取map列表
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    List<Map<String, Object>> customMapList(@Param("paramCondition") RestStockDetailsParam paramCondition);
//
//    /**
//     * 获取分页实体列表
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    Page<RestStockDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestStockDetailsParam paramCondition, @Param("dataScope") DataScope dataScope);
//
//    /**
//     * 获取分页map列表
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestStockDetailsParam paramCondition);
//
//
//    List<RestStockDetails> maintenanceQuerry(@Param("paramCondition") RestStockDetailsParam paramCondition);
//
//
//    Integer getNumberByStock(@Param("skuId") Long skuId, @Param("brandId") Long brandId, @Param("positionId") Long positionId);
//
//    List<Long> getInkindIds(@Param("paramCondition") RestStockDetailsParam param);
//
//    List<RestStockDetailsResult> stockInKindList();
////    List<StockDetailView> stockDetailView();
//    //综合统计
////    Page<StockDetailView> dataStatisticsView(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
//    Page<RestStockDetailsResult> skuDetailView(@Param("page") Page page,@Param("dataScope") DataScope dataScope,@Param("paramCondition") RestStockDetailsParam paramCondition);
////    List<StockDetailView> dataStatisticsViewDetail(@Param("paramCondition") DataStatisticsViewParam paramCondition);
////    StockDetailView stockNumberCycle(@Param("paramCondition") DataStatisticsViewParam paramCondition);
////    List<StockDetailView> stockNumberCycleDetail(@Param("paramCondition") DataStatisticsViewParam paramCondition);
}

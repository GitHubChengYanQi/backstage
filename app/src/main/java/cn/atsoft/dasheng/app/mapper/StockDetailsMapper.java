package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.request.StockDetailView;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库物品明细表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
public interface StockDetailsMapper extends BaseMapper<StockDetails> {

    /**
     * 获取列表
     *
     * @author
     * @Date 2021-07-15
     */
    List<StockDetailsResult> customList(@Param("paramCondition") StockDetailsParam paramCondition);



    List<StockDetailExcel> stockDetailExcelExport();

    /**
     * 获取map列表
     *
     * @author
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author
     * @Date 2021-07-15
     */
    Page<StockDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockDetailsParam paramCondition, @Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockDetailsParam paramCondition);


    List<StockDetails> maintenanceQuerry(@Param("paramCondition") StockDetailsParam paramCondition);


    Integer getNumberByStock(@Param("skuId") Long skuId, @Param("brandId") Long brandId, @Param("positionId") Long positionId);

    List<Long> getInkindIds(@Param("paramCondition") StockDetailsParam param);

    List<StockDetailsResult> stockInKindList();
    List<StockDetailView> stockDetailView();
    //综合统计
    Page<StockDetailView> dataStatisticsView(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockDetailsResult> skuDetailView(@Param("page") Page page,@Param("dataScope") DataScope dataScope,@Param("paramCondition") StockDetailsParam paramCondition);
    List<StockDetailView> dataStatisticsViewDetail(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    StockDetailView stockNumberCycle(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    List<StockDetailView> stockNumberCycleDetail(@Param("paramCondition") DataStatisticsViewParam paramCondition);
}

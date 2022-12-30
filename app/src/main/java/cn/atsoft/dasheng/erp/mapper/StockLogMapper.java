package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.app.model.request.StockView;
import cn.atsoft.dasheng.erp.entity.StockLog;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.params.StockLogParam;
import cn.atsoft.dasheng.erp.model.result.StockLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存操作记录主表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
public interface StockLogMapper extends BaseMapper<StockLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<StockLogResult> customList(@Param("paramCondition") StockLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<StockLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockLogParam paramCondition);

    List<StockLogResult> viewByUserAndType(@Param("paramCondition") DataStatisticsViewParam paramCondition);
    Page<StockView> groupView(@Param("page") Page page, @Param("paramCondition") DataStatisticsViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockLogParam paramCondition);

}

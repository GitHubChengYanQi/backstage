package cn.atsoft.dasheng.stockLog.mapper;

import cn.atsoft.dasheng.stockLog.entity.RestStockLog;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RestStockLogMapper extends BaseMapper<RestStockLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<RestStockLogResult> customList(@Param("paramCondition") RestStockLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestStockLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<RestStockLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestStockLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestStockLogParam paramCondition);

}

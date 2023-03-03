package cn.atsoft.dasheng.stockLog.mapper;

import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogDetailParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存操作记录子表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
public interface RestStockLogDetailMapper extends BaseMapper<RestStockLogDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<RestStockLogDetailResult> customList(@Param("paramCondition") RestStockLogDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestStockLogDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<RestStockLogDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestStockLogDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestStockLogDetailParam paramCondition);

}

package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库总表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<StockResult> customList(@Param("paramCondition") StockParam paramCondition);

    List<StockResult> item(@Param("paramCondition") StockParam paramCondition);

    List<StockResult> brand(@Param("paramCondition") StockParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<StockResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockParam paramCondition);

}

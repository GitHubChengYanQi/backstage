package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.StockForewarn;
import cn.atsoft.dasheng.erp.model.params.StockForewarnParam;
import cn.atsoft.dasheng.erp.model.result.StockForewarnResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存预警设置 Mapper 接口
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
public interface StockForewarnMapper extends BaseMapper<StockForewarn> {

    /**
     * 获取列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    List<StockForewarnResult> customList(@Param("paramCondition") StockForewarnParam paramCondition);

    /**
     * 获取map列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockForewarnParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    Page<StockForewarnResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockForewarnParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockForewarnParam paramCondition);

}

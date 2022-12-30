package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.model.params.StockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.StockLogDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface StockLogDetailMapper extends BaseMapper<StockLogDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<StockLogDetailResult> customList(@Param("paramCondition") StockLogDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockLogDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<StockLogDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockLogDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockLogDetailParam paramCondition);

}

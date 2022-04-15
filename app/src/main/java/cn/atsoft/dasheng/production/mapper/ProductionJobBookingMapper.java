package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionJobBooking;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报工表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
public interface ProductionJobBookingMapper extends BaseMapper<ProductionJobBooking> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<ProductionJobBookingResult> customList(@Param("paramCondition") ProductionJobBookingParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionJobBookingParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    Page<ProductionJobBookingResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionJobBookingParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionJobBookingParam paramCondition);

}

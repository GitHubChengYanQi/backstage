package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报工详情表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
public interface ProductionJobBookingDetailMapper extends BaseMapper<ProductionJobBookingDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<ProductionJobBookingDetailResult> customList(@Param("paramCondition") ProductionJobBookingDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionJobBookingDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    Page<ProductionJobBookingDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionJobBookingDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionJobBookingDetailParam paramCondition);

}

package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface ProcurementOrderDetailMapper extends BaseMapper<ProcurementOrderDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<ProcurementOrderDetailResult> customList(@Param("paramCondition") ProcurementOrderDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProcurementOrderDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<ProcurementOrderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProcurementOrderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProcurementOrderDetailParam paramCondition);

}

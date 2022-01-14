package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface ProcurementOrderMapper extends BaseMapper<ProcurementOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<ProcurementOrderResult> customList(@Param("paramCondition") ProcurementOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProcurementOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<ProcurementOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProcurementOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-01-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProcurementOrderParam paramCondition);

}

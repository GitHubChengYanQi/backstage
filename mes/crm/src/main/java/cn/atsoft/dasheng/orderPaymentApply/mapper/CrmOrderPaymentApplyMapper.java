package cn.atsoft.dasheng.orderPaymentApply.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.model.params.CrmOrderPaymentApplyParam;
import cn.atsoft.dasheng.orderPaymentApply.model.result.CrmOrderPaymentApplyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
public interface CrmOrderPaymentApplyMapper extends BaseMapper<CrmOrderPaymentApply> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<CrmOrderPaymentApplyResult> customList(@Param("paramCondition") CrmOrderPaymentApplyParam paramCondition);
    CrmOrderPaymentApplyResult getOneById(@Param("id") String id);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmOrderPaymentApplyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    Page<CrmOrderPaymentApplyResult> customPageList(@Param("dataScope") DataScope dataScope, @Param("page") Page page, @Param("paramCondition") CrmOrderPaymentApplyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmOrderPaymentApplyParam paramCondition);

}

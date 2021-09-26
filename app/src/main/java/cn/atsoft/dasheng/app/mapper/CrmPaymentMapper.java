package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmPayment;
import cn.atsoft.dasheng.app.model.params.CrmPaymentParam;
import cn.atsoft.dasheng.app.model.result.CrmPaymentResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款信息表 Mapper 接口
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
public interface CrmPaymentMapper extends BaseMapper<CrmPayment> {

    /**
     * 获取列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<CrmPaymentResult> customList(@Param("paramCondition") CrmPaymentParam paramCondition);

    /**
     * 获取map列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmPaymentParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    Page<CrmPaymentResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmPaymentParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmPaymentParam paramCondition);

}

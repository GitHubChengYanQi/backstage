package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款记录 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-03-01
 */
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-03-01
     */
    List<PaymentRecordResult> customList(@Param("paramCondition") PaymentRecordParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-03-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PaymentRecordParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-03-01
     */
    Page<PaymentRecordResult> customPageList(@Param("page") Page page, @Param("paramCondition") PaymentRecordParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-03-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PaymentRecordParam paramCondition);
    PaymentRecord getPaymentApplyMoney(@Param("spNo") String spNo);
    void updateApplyStatus(@Param("spNo") String spNo);

}

package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Payment;
import cn.atsoft.dasheng.crm.model.params.PaymentParam;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款信息表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<PaymentResult> customList(@Param("paramCondition") PaymentParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PaymentParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<PaymentResult> customPageList(@Param("page") Page page, @Param("paramCondition") PaymentParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PaymentParam paramCondition);

}

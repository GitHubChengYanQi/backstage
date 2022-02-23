package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款详情 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface PaymentDetailMapper extends BaseMapper<PaymentDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<PaymentDetailResult> customList(@Param("paramCondition") PaymentDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PaymentDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<PaymentDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") PaymentDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PaymentDetailParam paramCondition);

}

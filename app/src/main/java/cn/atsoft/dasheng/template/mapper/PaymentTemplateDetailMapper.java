package cn.atsoft.dasheng.template.mapper;

import cn.atsoft.dasheng.template.entity.PaymentTemplateDetail;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateDetailParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款模板详情 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface PaymentTemplateDetailMapper extends BaseMapper<PaymentTemplateDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<PaymentTemplateDetailResult> customList(@Param("paramCondition") PaymentTemplateDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PaymentTemplateDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<PaymentTemplateDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") PaymentTemplateDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PaymentTemplateDetailParam paramCondition);

}

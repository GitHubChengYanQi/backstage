package cn.atsoft.dasheng.template.mapper;

import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 付款模板 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface PaymentTemplateMapper extends BaseMapper<PaymentTemplate> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<PaymentTemplateResult> customList(@Param("paramCondition") PaymentTemplateParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PaymentTemplateParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<PaymentTemplateResult> customPageList(@Param("page") Page page, @Param("paramCondition") PaymentTemplateParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PaymentTemplateParam paramCondition);

}

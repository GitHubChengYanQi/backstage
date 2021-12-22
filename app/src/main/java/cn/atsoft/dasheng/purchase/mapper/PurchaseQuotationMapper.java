package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购报价表单 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-22
 */
public interface PurchaseQuotationMapper extends BaseMapper<PurchaseQuotation> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    List<PurchaseQuotationResult> customList(@Param("paramCondition") PurchaseQuotationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PurchaseQuotationParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    Page<PurchaseQuotationResult> customPageList(@Param("page") Page page, @Param("paramCondition") PurchaseQuotationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PurchaseQuotationParam paramCondition);

}

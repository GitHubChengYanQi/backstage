package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购清单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
public interface PurchaseListingMapper extends BaseMapper<PurchaseListing> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-15
     */
    List<PurchaseListingResult> customList(@Param("paramCondition") PurchaseListingParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PurchaseListingParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-15
     */
    Page<PurchaseListingResult> customPageList(@Param("page") Page page, @Param("paramCondition") PurchaseListingParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PurchaseListingParam paramCondition);


    Page<PurchaseListingResult> readyBuy (@Param("page") Page page, @Param("paramCondition") PurchaseListingParam paramCondition);
}

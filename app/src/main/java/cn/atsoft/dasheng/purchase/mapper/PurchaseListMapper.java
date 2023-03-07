package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.PurchaseList;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预购管理 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
public interface PurchaseListMapper extends BaseMapper<PurchaseList> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    List<PurchaseListResult> customList(@Param("paramCondition") PurchaseListParam paramCondition);
    List<PurchaseListResult> leftJoinList(@Param("paramCondition") PurchaseListParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PurchaseListParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    Page<PurchaseListResult> customPageList(@Param("page") Page page, @Param("paramCondition") PurchaseListParam paramCondition);
    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    Page<PurchaseListResult> supplyPageList(@Param("page") Page page, @Param("paramCondition") PurchaseListParam paramCondition);
    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
//    Page<Long> customPageList(@Param("page") Page page, @Param("paramCondition") PurchaseListParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PurchaseListParam paramCondition);

}

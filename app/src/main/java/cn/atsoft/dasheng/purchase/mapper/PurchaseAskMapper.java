package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购申请 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
public interface PurchaseAskMapper extends BaseMapper<PurchaseAsk> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-15
     */
    List<PurchaseAskResult> customList(@Param("paramCondition") PurchaseAskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PurchaseAskParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-15
     */
    Page<PurchaseAskResult> customPageList(@Param("page") Page page, @Param("paramCondition") PurchaseAskParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PurchaseAskParam paramCondition);

}

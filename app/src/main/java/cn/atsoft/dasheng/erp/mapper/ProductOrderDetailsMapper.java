package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderDetailsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品订单详情 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
public interface ProductOrderDetailsMapper extends BaseMapper<ProductOrderDetails> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-20
     */
    List<ProductOrderDetailsResult> customList(@Param("paramCondition") ProductOrderDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductOrderDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-20
     */
    Page<ProductOrderDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductOrderDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductOrderDetailsParam paramCondition);

}

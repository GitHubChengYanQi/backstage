package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品订单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-20
     */
    List<ProductOrderResult> customList(@Param("paramCondition") ProductOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-20
     */
    Page<ProductOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductOrderParam paramCondition);

}

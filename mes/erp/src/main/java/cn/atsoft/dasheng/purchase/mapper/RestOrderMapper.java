package cn.atsoft.dasheng.purchase.mapper;

import cn.atsoft.dasheng.purchase.entity.RestOrder;
import cn.atsoft.dasheng.purchase.model.params.RestOrderParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderResult;
import cn.atsoft.dasheng.purchase.model.result.RestOrderSimpleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface RestOrderMapper extends BaseMapper<RestOrder> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<RestOrderResult> customList(@Param("paramCondition") RestOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<RestOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestOrderParam paramCondition);



    Page<RestOrderSimpleResult> simpleCustomPageList(@Param("page") Page page, @Param("paramCondition") RestOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestOrderParam paramCondition);

}

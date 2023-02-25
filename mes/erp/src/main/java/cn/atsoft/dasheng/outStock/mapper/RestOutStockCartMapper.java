package cn.atsoft.dasheng.outStock.mapper;

import cn.atsoft.dasheng.outStock.entity.RestOutStockCart;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockCartParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockCartResult;
//import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
//import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
//import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
//import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单详情表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface RestOutStockCartMapper extends BaseMapper<RestOutStockCart> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<RestOutStockCartResult> customList(@Param("paramCondition") RestOutStockCartParam paramCondition);
    List<RestOutStockCartResult> selfCartList(@Param("paramCondition") RestOutStockCartParam paramCondition,@Param("userId") Long userId);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestOutStockCartParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<RestOutStockCartResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestOutStockCartParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestOutStockCartParam paramCondition);

    List<Long> lockInkind();
//
//    Integer lockNumber(@Param("paramCondition") QuerryLockedParam param);
//    List<Long> lockInkind(@Param("paramCondition") QuerryLockedParam param);

}

package cn.atsoft.dasheng.goods.sku.mapper;


import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.goods.sku.model.params.SkuListParam;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku表	 Mapper 接口
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
public interface RestSkuMapper extends BaseMapper<RestSku> {

    /**
     * 获取列表
     *
     * @author
     * @Date 2021-10-18
     */
    List<RestSkuResult> customList(@Param("paramCondition") RestSkuParam paramCondition);

    /**
     * 获取map列表
     *
     * @author
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestSkuParam paramCondition);


    /**
     * 获取分页实体列表
     *
     * @author
     * @Date 2021-10-18
     */
    Page<RestSkuResult> customPageList(@Param("spuIds") List<Long> spuIds, @Param("page") Page page, @Param("paramCondition") RestSkuParam paramCondition);

    /**
     * 选择物料查询接口
     *
     * @param spuIds
     * @param page
     * @param paramCondition
     * @return
     */
    Page<RestSkuResult> changeCustomPageList(@Param("spuIds") List<Long> spuIds, @Param("page") Page page, @Param("paramCondition") RestSkuParam paramCondition);


    List<RestSkuResult> allList(@Param("spuIds") List<Long> spuIds, @Param("paramCondition") RestSkuParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestSkuParam paramCondition);


//    List<SkuBind> skuBindList(@Param("SkuBindParam") SkuBindParam skuBindParam);

    List<RestSku> restSkuResultBySpuId(@Param("spuId") Long spuId);

    List<RestSku> getByIds(@Param("skuIds") List<Long> skuIds);


    Page<SkuListResult> customPageListBySkuView(@Param("page") Page page , @Param("params") SkuListParam skuListParam);

    List<SkuListResult> customListBySkuView(@Param("params") SkuListParam skuListParam);

}

package cn.atsoft.dasheng.bom.mapper;

import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

public interface RestBomMapper extends BaseMapper<RestBom> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<RestBomResult> customList(@Param("paramCondition") RestBomParam bomParam);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestBomParam bomParam);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<RestBomParam> customPageList(@Param("page") Page page, @Param("paramCondition") RestBomParam bomParam);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestBomParam BomParam);

    Integer countBySkuIdAndName(@Param("skuId") Long skuId,@Param("version") String version);

    List<RestBom> getBySkuIds(@Param("skuIds") List<Long> skuIds);

}

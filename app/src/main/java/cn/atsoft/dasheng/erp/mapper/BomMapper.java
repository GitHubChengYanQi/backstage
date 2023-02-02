package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Bom;
import cn.atsoft.dasheng.erp.model.params.BomParam;
import cn.atsoft.dasheng.erp.model.result.BomResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BomMapper extends BaseMapper<Bom> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<BomResult> customList(@Param("paramCondition") BomParam bomParam);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BomParam bomParam);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<BomResult> customPageList(@Param("page") Page page, @Param("paramCondition") BomParam bomParam);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BomParam BomParam);

    Integer countBySkuIdAndName(@Param("skuId") Long skuId,@Param("version") String version);

}

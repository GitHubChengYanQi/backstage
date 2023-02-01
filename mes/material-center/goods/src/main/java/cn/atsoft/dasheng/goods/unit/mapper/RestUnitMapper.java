package cn.atsoft.dasheng.goods.unit.mapper;


import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.params.RestUnitParam;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单位表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
public interface RestUnitMapper extends BaseMapper<RestUnit> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<RestUnitResult> customList(@Param("paramCondition") RestUnitParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestUnitParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<RestUnitResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestUnitParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestUnitParam paramCondition);

}

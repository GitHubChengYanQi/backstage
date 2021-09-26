package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.UnitParam;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
public interface UnitMapper extends BaseMapper<Unit> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<UnitResult> customList(@Param("paramCondition") UnitParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UnitParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<UnitResult> customPageList(@Param("page") Page page, @Param("paramCondition") UnitParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UnitParam paramCondition);

}

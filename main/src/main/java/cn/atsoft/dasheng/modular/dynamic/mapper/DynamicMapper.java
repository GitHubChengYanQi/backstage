package cn.atsoft.dasheng.modular.dynamic.mapper;

import cn.atsoft.dasheng.modular.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.modular.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.modular.dynamic.model.result.DynamicResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-10
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    List<DynamicResult> customList(@Param("paramCondition") DynamicParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DynamicParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    Page<DynamicResult> customPageList(@Param("page") Page page, @Param("paramCondition") DynamicParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DynamicParam paramCondition);

}

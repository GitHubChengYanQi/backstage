package cn.atsoft.dasheng.storehouse.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地点表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface RestStorehouseMapper extends BaseMapper<RestStorehouse> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<RestStorehouseResult> customList(@Param("paramCondition") RestStorehouseParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestStorehouseParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<RestStorehouseResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestStorehouseParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestStorehouseParam paramCondition);

}

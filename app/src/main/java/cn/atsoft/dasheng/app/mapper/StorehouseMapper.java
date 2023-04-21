package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.params.StorehouseParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
public interface StorehouseMapper extends BaseMapper<Storehouse> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<StorehouseResult> customList(@Param("paramCondition") StorehouseParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StorehouseParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<StorehouseResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehouseParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StorehouseParam paramCondition);
    List<Map<String, Object>> sumNumberByStorehouseIds(@Param("storehouseIds") List<Long> storehouseIds);

}

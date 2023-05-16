package cn.atsoft.dasheng.storehouse.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.model.params.StorehouseSpuClassBindParam;
import cn.atsoft.dasheng.storehouse.model.result.StorehouseSpuClassBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库物料分类绑定表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-15
 */
public interface StorehouseSpuClassBindMapper extends BaseMapper<StorehouseSpuClassBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    List<StorehouseSpuClassBindResult> customList(@Param("paramCondition") StorehouseSpuClassBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StorehouseSpuClassBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    Page<StorehouseSpuClassBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehouseSpuClassBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    Page<StorehouseSpuClassBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehouseSpuClassBindParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StorehouseSpuClassBindParam paramCondition);

}

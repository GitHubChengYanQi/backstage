package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.StorehousePositionsDeptBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsDeptBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位权限绑定表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-01-25
 */
public interface StorehousePositionsDeptBindMapper extends BaseMapper<StorehousePositionsDeptBind> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-01-25
     */
    List<StorehousePositionsDeptBindResult> customList(@Param("paramCondition") StorehousePositionsDeptBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-01-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StorehousePositionsDeptBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-01-25
     */
    Page<StorehousePositionsDeptBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsDeptBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-01-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StorehousePositionsDeptBindParam paramCondition);

}

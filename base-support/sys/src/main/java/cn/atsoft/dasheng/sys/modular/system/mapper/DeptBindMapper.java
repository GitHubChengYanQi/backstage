package cn.atsoft.dasheng.sys.modular.system.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.DeptBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.DeptBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DeptBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 各租户内 部门绑定表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-04
 */
public interface DeptBindMapper extends BaseMapper<DeptBind> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    List<DeptBindResult> customList(@Param("paramCondition") DeptBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DeptBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    Page<DeptBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") DeptBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    Page<DeptBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") DeptBindParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DeptBindParam paramCondition);

}

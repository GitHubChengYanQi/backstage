package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单据权限 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-05-18
 */
public interface DocumentsPermissionsMapper extends BaseMapper<DocumentsPermissions> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-05-18
     */
    List<DocumentsPermissionsResult> customList(@Param("paramCondition") DocumentsPermissionsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-05-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DocumentsPermissionsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-05-18
     */
    Page<DocumentsPermissionsResult> customPageList(@Param("page") Page page, @Param("paramCondition") DocumentsPermissionsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-05-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DocumentsPermissionsParam paramCondition);

}

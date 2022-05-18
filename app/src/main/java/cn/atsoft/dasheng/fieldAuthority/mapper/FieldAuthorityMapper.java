package cn.atsoft.dasheng.fieldAuthority.mapper;

import cn.atsoft.dasheng.fieldAuthority.entity.FieldAuthority;
import cn.atsoft.dasheng.fieldAuthority.model.params.FieldAuthorityParam;
import cn.atsoft.dasheng.fieldAuthority.model.result.FieldAuthorityResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字段操作权限表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-17
 */
public interface FieldAuthorityMapper extends BaseMapper<FieldAuthority> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    List<FieldAuthorityResult> customList(@Param("paramCondition") FieldAuthorityParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") FieldAuthorityParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    Page<FieldAuthorityResult> customPageList(@Param("page") Page page, @Param("paramCondition") FieldAuthorityParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") FieldAuthorityParam paramCondition);

}

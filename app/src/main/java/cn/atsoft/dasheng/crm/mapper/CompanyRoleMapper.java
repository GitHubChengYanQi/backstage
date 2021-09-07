package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.model.params.CompanyRoleParam;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公司角色表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
public interface CompanyRoleMapper extends BaseMapper<CompanyRole> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-06
     */
    List<CompanyRoleResult> customList(@Param("paramCondition") CompanyRoleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CompanyRoleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-06
     */
    Page<CompanyRoleResult> customPageList(@Param("page") Page page, @Param("paramCondition") CompanyRoleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CompanyRoleParam paramCondition);

}

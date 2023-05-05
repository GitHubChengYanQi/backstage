package cn.atsoft.dasheng.sys.modular.rest.mapper;

import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.node.ZTreeNode;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface RestRoleMapper extends BaseMapper<RestRole> {

    /**
     * 根据条件查询角色列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    Page<Map<String, Object>> selectRoles(@Param("page") Page page, @Param("condition") String condition,@Param("tenantId") Long tenantId);

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    int deleteRolesById(@Param("roleId") Long roleId);

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<CascaderNode> roleTreeList();

    List<TreeNode> roleTree(@Param("tenantId") Long tenantId);
    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<ZTreeNode> roleTreeListByRoleId(Long[] roleId);

}

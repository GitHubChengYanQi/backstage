package cn.atsoft.dasheng.sys.modular.rest.mapper;

import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.MenuNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface RestMenuMapper extends BaseMapper<RestMenu> {

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    Page<Map<String, Object>> selectMenus(@Param("page") Page page, @Param("condition") String condition, @Param("level") String level, @Param("menuId") Long menuId, @Param("code") String code);

    /**
     * 根据条件查询菜单
     */
    List<String> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取菜单的名称
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<String> getMenuNamesByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<CascaderNode> treeviewNodes();

    List<TreeNode> treeNodes();

    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<TreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    /**
     * 删除menu关联的relation
     *
     * @param menuId
     * @return
     * @date 2017年2月19日 下午4:10:59
     */
    int deleteRelationByMenu(@Param("menuId") Long menuId);

    /**
     * 获取资源url通过角色id
     *
     * @param roleId
     * @return
     * @date 2017年2月19日 下午7:12:38
     */
    List<String> getResUrlsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     * @date 2017年2月19日 下午10:35:40
     */
    List<MenuNode> getMenusByRoleIds(List<Long> roleIds);

    List<MenuNode> getMobileMenusByRoleIds(@Param("roleIds")List<Long> roleIds,@Param("type")Integer type);

    /**
     * 根据角色获取菜单的类型列表
     *
     * @param roleIds
     * @return
     * @date 2019年07月11日16:26:27
     */
    List<String> getMenusTypesByRoleIds(List<Long> roleIds);

    /**
     * 查询菜单树形列表
     *
     * @author fengshuonan
     * @Date 2019/2/23 22:03
     */
    List<Map<String, Object>> selectMenuTree(@Param("condition") String condition, @Param("level") String level);

    /**
     * 获取pcodes like某个code的菜单列表
     *
     * @author fengshuonan
     * @Date 2019/3/31 15:51
     */
    List<RestMenu> getMenusLikePcodes(@Param("code") String code);

}

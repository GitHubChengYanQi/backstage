package cn.atsoft.dasheng.sys.modular.rest.service;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.sys.core.constant.Const;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.log.LogObjectHolder;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestRelation;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestRole;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestRelationMapper;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestRoleMapper;
import cn.atsoft.dasheng.sys.modular.system.model.RoleDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.node.ZTreeNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class RestRoleService extends ServiceImpl<RestRoleMapper, RestRole> {

    @Resource
    private RestRoleMapper restRoleMapper;

    @Resource
    private RestRelationMapper restRelationMapper;

    @Resource
    private RestUserService restUserService;

    /**
     * 添加角色
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:40 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleDto role) {

        if (ToolUtil.isOneEmpty(role, role.getName(), role.getPid(), role.getDescription())) {
            throw new RequestEmptyException();
        }

        role.setRoleId(null);

        RestRole restRole = this.roleSetPids(role);

        this.save(restRole);
    }

    /**
     * 编辑角色
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:40 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void editRole(RoleDto roleDto) {

        if (ToolUtil.isOneEmpty(roleDto, roleDto.getName(), roleDto.getPid(), roleDto.getDescription())) {
            throw new RequestEmptyException();
        }

        RestRole old = this.getById(roleDto.getRoleId());
        BeanUtil.copyProperties(roleDto, old);

        RestRole restRole = this.roleSetPids(roleDto);

        this.updateById(restRole);
    }

    /**
     * 设置某个角色的权限
     *
     * @param roleId 角色id
     * @param ids    权限的id
     * @date 2017年2月13日 下午8:26:53
     */
    @Transactional(rollbackFor = Exception.class)
    public void setAuthority(Long roleId, String ids) {

        // 删除该角色所有的权限
        this.restRoleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(ids.split(","))) {
            RestRelation relation = new RestRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            this.restRelationMapper.insert(relation);
        }

        // 刷新当前用户的权限
        restUserService.refreshCurrentUser();
    }

    /**
     * 删除角色
     *
     * @author stylefeng
     * @Date 2017/5/5 22:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRoleById(Long roleId) {

        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        //删除角色
        this.restRoleMapper.deleteById(roleId);

        //删除该角色所有的权限
        this.restRoleMapper.deleteRolesById(roleId);
    }

    /**
     * 根据条件查询角色列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    public Page<Map<String, Object>> selectRoles(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRoles(page, condition,LoginContextHolder.getContext().getTenantId());
    }

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    public int deleteRolesById(Long roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    public List<CascaderNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    public List<TreeNode> roleTree() {
        return this.baseMapper.roleTree(LoginContextHolder.getContext().getTenantId());
    }

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    public List<ZTreeNode> roleTreeListByRoleId(Long[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

    public RestRole roleSetPids(RoleDto roleDto) {
        RestRole restRole = new RestRole();
        BeanUtils.copyProperties(roleDto, restRole);

        if (ToolUtil.isEmpty(roleDto.getPid()) || roleDto.getPid().equals(0L)) {
            restRole.setPid(0L);
            restRole.setPids("0,");
        } else {
            Long pid = roleDto.getPid();
            RestRole pRole = this.getById(pid);

            if (roleDto.getPid().equals(roleDto.getRoleId())) {
                throw new ServiceException(500, "上级角色选择错误");
            }
            restRole.setPid(pRole.getRoleId());
            restRole.setPids(pRole.getPids() + pRole.getRoleId() + ",");
        }
        return restRole;
    }

}

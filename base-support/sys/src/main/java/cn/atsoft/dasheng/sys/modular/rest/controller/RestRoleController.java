package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.core.treebuild.DefaultCascaderBuildFactory;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.sys.core.constant.dictmap.DeleteDict;
import cn.atsoft.dasheng.sys.core.constant.dictmap.RoleDict;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestRole;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUser;
import cn.atsoft.dasheng.sys.modular.rest.model.params.RoleSetParam;
import cn.atsoft.dasheng.sys.modular.system.factory.CascaderFactory;
import cn.atsoft.dasheng.sys.modular.system.model.RoleDto;
import cn.atsoft.dasheng.sys.modular.system.warpper.RoleWrapper;
import cn.hutool.core.bean.BeanUtil;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.CascaderNode;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.service.RestMenuService;
import cn.atsoft.dasheng.sys.modular.rest.service.RestRoleService;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/rest/role")
public class RestRoleController extends BaseController {

    @Autowired
    private RestUserService restUserService;

    @Autowired
    private RestRoleService restRoleService;

    @Autowired
    private RestMenuService restMenuService;

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/list")
    public PageInfo list(@RequestParam(value = "roleName", required = false) String roleName) {
        Page<Map<String, Object>> roles = this.restRoleService.selectRoles(roleName);
        Page<Map<String, Object>> wrap = new RoleWrapper(roles).wrap();
        return PageFactory.createPageInfo(wrap);
    }

    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    public ResponseData add(@RequestBody RoleDto restRole) {
        this.restRoleService.addRole(restRole);
        return SUCCESS_TIP;
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    public ResponseData edit(@RequestBody RoleDto roleDto) {
        this.restRoleService.editRole(roleDto);
        return SUCCESS_TIP;
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = DeleteDict.class)
    public ResponseData remove(@RequestParam Long roleId) {
        this.restRoleService.delRoleById(roleId);
        return SUCCESS_TIP;
    }

    /**
     * 查看角色
     */
    @RequestMapping(value = "/view")
    public ResponseData view(@RequestParam Long roleId) {
//        Long roleId = roleParam.getRoleId();
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        RestRole role = this.restRoleService.getById(roleId);
        Map<String, Object> roleMap = BeanUtil.beanToMap(role);

        Long pid = role.getPid();
        String pName = ConstantFactory.me().getSingleRoleName(pid);
        roleMap.put("pName", pName);

        //获取角色绑定的菜单
        List<String> menuIds = this.restMenuService.getMenuIdsByRoleId(roleId);
        List<String> menuNames = this.restMenuService.getBaseMapper().getMenuNamesByRoleId(roleId);
        if (ToolUtil.isNotEmpty(menuIds)) {
            roleMap.put("menuIds", menuIds);
            roleMap.put("menuNames", menuNames);
        } else {
            roleMap.put("menuIds", new ArrayList<>());
            roleMap.put("menuNames", new ArrayList<>());
        }

        return ResponseData.success(roleMap);
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    public ResponseData setAuthority(@RequestBody RoleSetParam roleSetParam) {
        Long roleId = roleSetParam.getRoleId();
        String ids = roleSetParam.getIds();
        if (ToolUtil.isOneEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.restRoleService.setAuthority(roleId, ids);
        return SUCCESS_TIP;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    public ResponseData roleTreeList() {
        List<CascaderNode> roleTreeList = this.restRoleService.roleTreeList();
        roleTreeList.add(CascaderFactory.createRoot());

        //构建树
        DefaultCascaderBuildFactory<CascaderNode> factory = new DefaultCascaderBuildFactory<>();
        factory.setRootParentId("-1");
        List<CascaderNode> results = factory.doTreeBuild(roleTreeList);

        return ResponseData.success(results);
    }

    @RequestMapping(value = "/roleTree")
    public ResponseData roleTree() {
        List<TreeNode> roleTree = this.restRoleService.roleTree();
        roleTree.add(TreeNode.createParent());

        //构建树
        DefaultTreeBuildFactory<TreeNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("-1");
        List<TreeNode> results = factory.doTreeBuild(roleTree);

        return ResponseData.success(results);
    }

    /**
     * 获取角色列表，通过用户id
     */
    @RequestMapping(value = "/roleTreeListByUserId")
    public ResponseData roleTreeListByUserId(@RequestParam Long userId) {
        RestUser theUser = this.restUserService.getById(userId);
        String roleId = theUser.getRoleId();

        String[] strArray = null;
        if (ToolUtil.isNotEmpty(roleId)) {
            strArray = roleId.split(",");
        }
        Map<String,String[]> result = new HashMap<>();
        result.put("checked",strArray);
        return ResponseData.success(result);
    }

}

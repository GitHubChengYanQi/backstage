package cn.atsoft.dasheng.sys.modular.rest.service;

import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.sys.core.constant.Const;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.constant.state.ManagerStatus;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.util.DefaultImages;
import cn.atsoft.dasheng.sys.core.util.SaltUtil;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUser;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUserPos;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestUserMapper;
import cn.atsoft.dasheng.sys.modular.rest.model.params.MobileUrl;
import cn.atsoft.dasheng.sys.modular.system.entity.*;
import cn.atsoft.dasheng.sys.modular.system.model.UserDto;
import cn.atsoft.dasheng.sys.modular.system.model.params.DeptBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DeptBindResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.oauth2.entity.OauthUserInfo;
import cn.atsoft.dasheng.base.oauth2.service.OauthUserInfoService;
import cn.atsoft.dasheng.base.pojo.node.MenuNode;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.sys.modular.rest.factory.RestUserFactory;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class RestUserService extends ServiceImpl<RestUserMapper, RestUser> {

    @Resource
    private RestMenuService restMenuService;

    @Resource
    private RestUserPosService restUserPosService;

    @Resource
    private DeptService deptService;
    @Resource
    private DeptBindService deptBindService;

    @Resource
    private RoleService roleService;
    @Resource
    private RestMediaService restMediaService;
    @Resource
    private TenantService tenantService;
    @Resource
    private TenantBindService tenantBindService;
    @Resource
    private UserService userService;

    /**
     * 添加用戶
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto user) {
        if (ToolUtil.isEmpty(user.getPhone())) {
            throw new ServiceException(500, "请填写手机号");
        } else {
            Integer count = this.lambdaQuery().eq(RestUser::getPhone, user.getPhone()).count();
            if (count > 0) {
                throw new ServiceException(500, "手机号不能重复");
            }
        }
        deptBind(user);

        // 判断账号是否重复
        RestUser theUser = this.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        String salt = SaltUtil.getRandomSalt();
        String password = SaltUtil.md5Encrypt(user.getPassword(), salt);

        RestUser newUser = RestUserFactory.createRestUser(user, password, salt);
        this.save(newUser);

        //添加职位关联
        addPosition(user.getPosition(), newUser.getUserId());
    }

    /**
     * 修改用户
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:53
     */
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserDto user) {
        RestUser oldUser = this.getById(user.getUserId());
        if (oldUser == null) {
            throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
        }
        user.setPhone(null);
        if (oldUser == null) {
            throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
        }
        deptBind(user);
//        if (LoginContextHolder.getContext().hasRole(Const.ADMIN_NAME)) {
            this.updateById(RestUserFactory.editRestUser(user, oldUser));
//        } else {
//            this.assertAuth(user.getUserId());
//            LoginUser shiroUser = LoginContextHolder.getContext().getUser();
//            if (shiroUser.getId().equals(user.getUserId())) {
//                this.updateById(RestUserFactory.editRestUser(user, oldUser));
//            } else {
//                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
//            }
//        }

        //删除职位关联
        restUserPosService.remove(new QueryWrapper<RestUserPos>().eq("user_id", user.getUserId()).eq("tenant_id", LoginContextHolder.getContext().getTenantId()));

        //添加职位关联
        addPosition(user.getPosition(), user.getUserId());
    }
    /**
     * 修改部门
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:53
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeDept(UserDto user) {
        RestUser oldUser = this.getById(user.getUserId());
        if (oldUser == null) {
            throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
        }
        user.setPhone(null);
        if (oldUser == null) {
            throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
        }
        deptBind(user);
//        if (LoginContextHolder.getContext().hasRole(Const.ADMIN_NAME)) {
            this.updateById(RestUserFactory.editRestUser(user, oldUser));
//        } else {
//            this.assertAuth(user.getUserId());
//            LoginUser shiroUser = LoginContextHolder.getContext().getUser();
//            if (shiroUser.getId().equals(user.getUserId())) {
//                this.updateById(RestUserFactory.editRestUser(user, oldUser));
//            } else {
//                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
//            }
//        }

    }
    public void deptBind(UserDto user){
        //删除部门关联
        deptBindService.remove(new QueryWrapper<DeptBind>().eq("user_id", user.getUserId()).eq("tenant_id", LoginContextHolder.getContext().getTenantId()));

        if (ToolUtil.isNotEmpty(user.getDeptList())){
            //添加部门关联
            List<DeptBind> deptBinds = new ArrayList<>();
            int i = 0;
            for (DeptBindParam deptParam : user.getDeptList()) {
                DeptBind deptBind = new DeptBind();
                deptBind.setUserId(user.getUserId());
                deptBind.setDeptId(deptParam.getDeptId());
                deptBind.setTenantId(LoginContextHolder.getContext().getTenantId());
                deptBind.setMainDept(deptParam.getMainDept());
                if (ToolUtil.isNotEmpty(deptParam.getMainDept()) && deptParam.getMainDept().equals(1)){
                    i++;
                }
                deptBinds.add(deptBind);
            }
            if (i == 0){
                throw new ServiceException(500,"请选择一个主部门");
            }
            if (i > 1){
                throw new ServiceException(500,"主部门只能选择一个");
            }

            deptBindService.saveBatch(deptBinds);
        }
    }
    /**
     * 删除用户
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:54
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {

        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //不是超级管理员校验权限
        if (!LoginContextHolder.getContext().isAdmin()) {
            this.assertAuth(userId);
        }
        this.setStatus(userId, ManagerStatus.DELETED.getCode());

        //删除对应的oauth2绑定表
        OauthUserInfoService oauthUserInfoService = null;
        try {
            oauthUserInfoService = SpringContextHolder.getBean(OauthUserInfoService.class);
            oauthUserInfoService.remove(new QueryWrapper<OauthUserInfo>().eq("user_id", userId));
        } catch (Exception e) {
            //没有集成oauth2模块，不操作
        }

        //删除职位关联
        restUserPosService.remove(new QueryWrapper<RestUserPos>().eq("user_id", userId));
    }

    /**
     * 修改用户状态
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public int setStatus(Long userId, String status) {
        return this.baseMapper.setStatus(userId, status);
    }

    /**
     * 修改密码
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public void changePwd(String oldPassword, String newPassword) {
        Long userId = LoginContextHolder.getContext().getUser().getId();
        RestUser user = this.getById(userId);

        String oldMd5 = SaltUtil.md5Encrypt(oldPassword, user.getSalt());

        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = SaltUtil.md5Encrypt(newPassword, user.getSalt());
            user.setPassword(newMd5);
            this.updateById(user);
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 根据条件查询用户列表
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = PageFactory.defaultPage();
        return this.baseMapper.selectUsers(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 设置用户的角色
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public int setRoles(Long userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    /**
     * 通过账号获取用户
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:46
     */
    public RestUser getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    /**
     * 获取用户菜单列表
     */
    public List<Map<String, Object>> getUserMenuNodes(List<Long> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> menus = restMenuService.getMenusByRoleIds(roleList);

            //定义不同系统分类的菜单集合
            ArrayList<Map<String, Object>> lists = new ArrayList<>();

            //根据当前用户包含的系统类型，分类出不同的菜单
            List<Map<String, Object>> systemTypes = LoginContextHolder.getContext().getUser().getSystemTypes();
            for (Map<String, Object> systemType : systemTypes) {

                //当前遍历系统分类code
                String systemCode = (String) systemType.get("code");

                //获取当前系统分类下菜单集合
                ArrayList<MenuNode> originSystemTypeMenus = new ArrayList<>();
                for (MenuNode menu : menus) {
                    if (menu.getSystemType().equals(systemCode)) {
                        originSystemTypeMenus.add(menu);
                    }
                }

                //拼接存放key为系统分类编码，value为该分类下菜单集合的map
                HashMap<String, Object> map = new HashMap<>();
                List<MenuNode> treeSystemTypeMenus = MenuNode.buildTitle(originSystemTypeMenus);

                //渲染系统类型id
                map.put("id", systemCode);

                //系统类型的名称
                map.put("name", ConstantFactory.me().getDictNameByCode(systemCode));

                //各个系统地子菜单
                map.put("subMenus", treeSystemTypeMenus);

                lists.add(map);
            }

            return lists;

        }

    }


//    private ArrayList<Map<String, Object>> userMenuNodes(List<MenuNode> menus) {
//
//    }

    /**
     * 获取用户菜单列表
     */
    public List<Map<String, Object>> getUserMobielMenuNodes(List<Long> roleList,Integer type) {
//        if (roleList == null || roleList.size() == 0) {
//            return new ArrayList<>();
//        } else {
            List<MenuNode> menus = restMenuService.getMobileMenusByRoleIds(roleList,type);

            //定义不同系统分类的菜单集合
            ArrayList<Map<String, Object>> lists = new ArrayList<>();

            //根据当前用户包含的系统类型，分类出不同的菜单
            List<Map<String, Object>> systemTypes = LoginContextHolder.getContext().getUser().getSystemTypes();
            for (Map<String, Object> systemType : systemTypes) {

                //当前遍历系统分类code
                String systemCode = (String) systemType.get("code");

                //获取当前系统分类下菜单集合
                ArrayList<MenuNode> originSystemTypeMenus = new ArrayList<>();
                for (MenuNode menu : menus) {
                    if (menu.getSystemType().equals(systemCode)) {
                        originSystemTypeMenus.add(menu);
                    }
                }

                //拼接存放key为系统分类编码，value为该分类下菜单集合的map
                HashMap<String, Object> map = new HashMap<>();
                List<MenuNode> treeSystemTypeMenus = MenuNode.buildMobileTitle(originSystemTypeMenus);

                //渲染系统类型id
                map.put("id", systemCode);

                //系统类型的名称
                map.put("name", ConstantFactory.me().getDictNameByCode(systemCode));

                //各个系统地子菜单
                map.put("subMenus", treeSystemTypeMenus);

                lists.add(map);
            }

            return lists;
//        }

    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    public void assertAuth(Long userId) {
        if (LoginContextHolder.getContext().isAdmin()) {
            return;
        }
        List<Long> deptDataScope = LoginContextHolder.getContext().getDeptDataScope();
        RestUser user = this.getById(userId);
        Long deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
//            throw new ServiceException(BizExceptionEnum.NO_PERMITION);

            return;
        }

    }

    /**
     * 刷新当前登录用户的信息
     *
     * @author fengshuonan
     * @Date 2019/1/19 5:59 PM
     */
    public void refreshCurrentUser() {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Long id = user.getId();
        RestUser currentUser = this.getById(id);

        //TODO 刷新
//        LoginUser shiroUser = userAuthService.shiroUser(currentUser);
//        LoginUser lastUser = LoginContextHolder.getContext().getUser();
//        BeanUtil.copyProperties(shiroUser, lCastUser);
    }

    /**
     * 获取用户首页信息
     *
     * @author fengshuonan
     * @Date 2019/10/17 16:18
     */
    public Map<String, Object> getUserIndexInfo() {

        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();
        //用户没有角色无法显示首页信息
        if (roleList == null || roleList.size() == 0) {
            return null;
        }

        List<Long> deptDataScope = LoginContextHolder.getContext().getDeptDataScope();  //当前角色部门
        if (ToolUtil.isEmpty(deptDataScope)) {
            deptDataScope = new ArrayList<>();
        }

        List<Dept> depts = deptDataScope.size() == 0 ? new ArrayList<>() : deptService.listByIds(deptDataScope);
        List<String> deptNames = new ArrayList<>();
        for (Dept dept : depts) {
            deptNames.add(dept.getFullName());
        }


        List<String> roleName = new ArrayList<>();
        List<Role> roles = roleService.listByIds(roleList);
        for (Role role : roles) {
            roleName.add(role.getName());
        }
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(Collections.singletonList(user.getId()));


        List<Map<String, Object>> menus = this.getUserMenuNodes(roleList);
        List<Map<String, Object>> mobielMenus = this.getUserMobielMenuNodes(roleList,1);
        List<Map<String, Object>> miniAppMenus = this.getUserMobielMenuNodes(null,2);
//        String portrait = this.baseMapper.headPortrait(LoginContextHolder.getContext().getUserId()); //获取企业微信头像
        HashMap<String, Object> result = new HashMap<>();
        result.put("menus", menus);
        result.put("mobielMenus", mobielMenus);
        result.put("miniAppMenus", miniAppMenus);
        result.put("miniAppAvatar",ToolUtil.isEmpty(userResultsByIds.get(0).getMiniAppAvatar())?"":userResultsByIds.get(0).getMiniAppAvatar());
        result.put("nickName",ToolUtil.isEmpty(userResultsByIds.get(0).getNickName())?"":userResultsByIds.get(0).getNickName());

        result.put("tenantId", "");
        result.put("tenantLogo", "");
        result.put("isTenantAdmin", null);
        Tenant tenant = ToolUtil.isEmpty(user.getTenantId()) ? null : tenantService.getById(user.getTenantId());
        if (ToolUtil.isNotEmpty(tenant) && tenant.getDisplay().equals(1)) {
            result.put("tenantId", tenant.getTenantId());
            if (ToolUtil.isNotEmpty(tenant.getCreateUser()) && tenant.getCreateUser().equals(user.getId())) {
                result.put("isTenantAdmin", true);
            } else {
                result.put("isTenantAdmin", false);
            }
            if (ToolUtil.isNotEmpty(tenant.getLogo())) {
                result.put("tenantLogo", restMediaService.getMediaUrlResults(Collections.singletonList(tenant.getLogo())).get(0));
            }
        }
        result.put("haveOtherTenant", true);
        if (ToolUtil.isEmpty(result.get("tenantId"))){
            List<Long> tenantIds = tenantBindService.lambdaQuery().eq(TenantBind::getUserId, user.getId()).eq(TenantBind::getStatus, 99).list().stream().map(TenantBind::getTenantId).distinct().collect(Collectors.toList());
            Integer count = tenantIds.size() == 0 ? 0 : tenantService.lambdaQuery().in(Tenant::getTenantId, tenantIds).eq(Tenant::getDisplay, 1).count();
            if (count>0){
                result.put("haveOtherTenant", true);
            }else {
                result.put("haveOtherTenant", false);
            }
        }
        result.put("tenantName", ToolUtil.isEmpty(user.getTenantName()) ? "" : user.getTenantName());
        result.put("avatar", ToolUtil.isNotEmpty(user.getAvatar()) ? user.getAvatar() : DefaultImages.defaultAvatarUrl());
        result.put("name", user.getName());
        result.put("phone", ToolUtil.isEmpty(user.getPhone()) ? "" : user.getPhone());
        result.put("id", user.getId());
        result.put("dept", deptNames);
        result.put("role", roleName);
        result.put("MobileUrl", MobileUrl.prefix);
        return result;
    }

    /**
     * 获取用户的基本信息
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:12
     */

    public Map<String, Object> getUserInfo(Long userId) {
        RestUser user = this.getById(userId);
        Map<String, Object> map = RestUserFactory.removeUnSafeFieldsRest(user);
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(Collections.singletonList(userId));
        HashMap<String, Object> hashMap = MapUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("nickName", ToolUtil.isEmpty(userResultsByIds.get(0).getNickName()) ? "" : userResultsByIds.get(0).getNickName());
        hashMap.put("miniAppAvatar", ToolUtil.isEmpty(userResultsByIds.get(0).getMiniAppAvatar()) ? "" : userResultsByIds.get(0).getMiniAppAvatar());
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));
        hashMap.put("deptList", new ArrayList<>());
        if (ToolUtil.isNotEmpty(LoginContextHolder.getContext().getTenantId())){
            List<DeptBindResult> deptBindResults = BeanUtil.copyToList(deptBindService.lambdaQuery().eq(DeptBind::getUserId, user.getUserId()).eq(DeptBind::getTenantId, LoginContextHolder.getContext().getTenantId()).list(), DeptBindResult.class);

            deptBindService.format(deptBindResults);
            hashMap.put("deptList",deptBindResults);

        }
        hashMap.put("positionIds", ConstantFactory.me().getPositionIds(userId).split(","));
        hashMap.put("positionNames", ConstantFactory.me().getPositionName(userId));


        return hashMap;
    }


    /**
     * 添加职位关联
     *
     * @author fengshuonan
     * @Date 2019-06-28 13:35
     */
    private void addPosition(String positions, Long userId) {
        if (ToolUtil.isNotEmpty(positions)) {
            String[] position = positions.split(",");
            for (String item : position) {

                RestUserPos entity = new RestUserPos();
                entity.setUserId(userId);
                entity.setPosId(Long.valueOf(item));

                restUserPosService.save(entity);

            }
        }
    }

}

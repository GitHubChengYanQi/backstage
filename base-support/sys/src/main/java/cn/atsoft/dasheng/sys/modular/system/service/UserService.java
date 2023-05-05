package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.sys.core.constant.Const;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.constant.state.ManagerStatus;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.util.DefaultImages;
import cn.atsoft.dasheng.sys.core.util.SaltUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import cn.atsoft.dasheng.sys.modular.system.entity.Role;
import cn.atsoft.dasheng.sys.modular.system.model.DeptDto;
import cn.atsoft.dasheng.sys.modular.system.model.RoleDto;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserParam;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserPosParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.oauth2.entity.OauthUserInfo;
import cn.atsoft.dasheng.base.oauth2.service.OauthUserInfoService;
import cn.atsoft.dasheng.base.pojo.node.MenuNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.entity.UserPos;
import cn.atsoft.dasheng.sys.modular.system.factory.UserFactory;
import cn.atsoft.dasheng.sys.modular.system.mapper.UserMapper;
import cn.atsoft.dasheng.sys.modular.system.model.UserDto;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleResult;
import javax.servlet.http.HttpServletRequest;
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
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserPosService userPosService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RestMediaService mediaService;

    /**
     * 添加用戶
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto user) {

        // 判断账号是否重复
        User theUser = this.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        String salt = SaltUtil.getRandomSalt();
        String password = SaltUtil.md5Encrypt(user.getPassword(), salt);

        User newUser = UserFactory.createUser(user, password, salt);
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
        User oldUser = this.getById(user.getUserId());

        if (LoginContextHolder.getContext().hasRole(Const.ADMIN_NAME)) {
            this.updateById(UserFactory.editUser(user, oldUser));
        } else {
            this.assertAuth(user.getUserId());
            LoginUser shiroUser = LoginContextHolder.getContext().getUser();
            if (shiroUser.getId().equals(user.getUserId())) {
                this.updateById(UserFactory.editUser(user, oldUser));
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }

        //删除职位关联
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", user.getUserId()));

        //添加职位关联
        addPosition(user.getPosition(), user.getUserId());
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
        this.assertAuth(userId);
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
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", userId));
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
        User user = this.getById(userId);

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
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
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
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    /**
     * 获取用户菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:46
     */
    public List<Map<String, Object>> getUserMenuNodes(List<Long> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);

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
                map.put("systemType", systemCode);
                map.put("menus", treeSystemTypeMenus);
                lists.add(map);
            }

            return lists;
        }
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
        User user = this.getById(userId);
        Long deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }
    }

    /**
     * 刷新当前登录用户的信息
     *
     * @author fengshuonan
     * @Date 2019/1/19 5:59 PM
     */
    public void refreshCurrentUser() {
        //TODO 刷新
    }

    /**
     * 获取用户的基本信息
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:12
     */
    public Map<String, Object> getUserInfo(Long userId) {
        User user = this.getById(userId);
        Map<String, Object> map = UserFactory.removeUnSafeFields(user);

        HashMap<String, Object> hashMap = MapUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));

        return hashMap;
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

        List<Map<String, Object>> menus = this.getUserMenuNodes(roleList);

        HashMap<String, Object> result = new HashMap<>();
        result.put("menus", menus);
        result.put("avatar", DefaultImages.defaultAvatarUrl());
        result.put("name", user.getName());

        return result;
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

                UserPos entity = new UserPos();
                entity.setUserId(userId);
                entity.setPosId(Long.valueOf(item));

                userPosService.save(entity);
            }
        }
    }

    /**
     * 选择办理人
     *
     * @author fengshuonan
     * @Date 2019-08-27 19:07
     */
    public IPage listUserAndRoleExpectAdmin(Page pageContext) {
        return baseMapper.listUserAndRoleExpectAdmin(pageContext);
    }

    public List<Long> getAllUsersId() {
        List<User> list = this.list();
        List<Long> userIds = new ArrayList<>();
        for (User user : list) {
            userIds.add(user.getUserId());
        }
        return userIds;
    }

    public List<User> getUserByPositionAndDept(Long deptId, List<Long> positionIds) {
        return this.baseMapper.listUserByPositionAndDept(deptId, positionIds);
    }

    public List<UserResult> getUserResultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        HttpServletRequest request = HttpContext.getRequest();
        assert request != null;
        String appid = request.getParameter("appid");
        List<UserResult> results = this.baseMapper.listUserByIds(ids,appid);
         this.format(results);
        return results;
    }
    public void format(List<UserResult> dataList) {

        List<Long> deptIds = dataList.stream().map(UserResult::getDeptId).distinct().collect(Collectors.toList());

        List<String> roleStrs = dataList.stream().map(UserResult::getRoleId).distinct().collect(Collectors.toList());

        List<Long> roleIds = new ArrayList<>();
        for (String roleStr : roleStrs) {
            try {
                List<Long> roleIdList = Arrays.stream(roleStr.split(",")).map(i -> Long.valueOf(i.trim())).collect(Collectors.toList());
                roleIds.addAll(roleIdList);
            } catch (Exception ignored) {

            }
        }

        roleIds = roleIds.stream().distinct().collect(Collectors.toList());

        List<Dept> depts = deptIds.size() == 0 ? new ArrayList<>() : deptService.listByIds(deptIds);

        List<Role> roles = roleIds.size() == 0 ? new ArrayList<>() : roleService.listByIds(roleIds);

        List<DeptDto> deptDtos = BeanUtil.copyToList(depts, DeptDto.class);

        List<RoleDto> roleDtos = BeanUtil.copyToList(roles, RoleDto.class);

        for (UserResult userResult : dataList) {
            for (DeptDto deptDto : deptDtos) {
                if (ToolUtil.isNotEmpty(userResult.getDeptId()) && userResult.getDeptId().equals(deptDto.getDeptId())) {
                    userResult.setDeptResult(deptDto);
                    break;
                }
            }
            try{
                String mediaUrl = mediaService.getMediaUrl(Long.parseLong(userResult.getMiniAppAvatar()), 0L);
                userResult.setMiniAppAvatar(mediaUrl);
            }catch (Exception e){

            }
            List<RoleDto> roleResultList = new ArrayList<>();
            for (RoleDto role : roleDtos) {
                if (ToolUtil.isNotEmpty(userResult.getRoleId())){
                    List<Long> roleIdList = Arrays.stream(userResult.getRoleId().split(",")).map(i -> Long.valueOf(i.trim())).collect(Collectors.toList());
                    for (Long roleId : roleIdList) {
                        if (role.getRoleId().equals(roleId)) {
                            roleResultList.add(role);
                        }
                    }
                }
            }
            userResult.setRoleResults(roleResultList.stream().distinct().collect(Collectors.toList()));
        }


    }

    /**
     * 用户分页
     *
     * @param param
     * @return
     */
    public Page<UserResult> userResultPageList(UserParam param) {
        Page<UserResult> userResultPage = this.baseMapper.userResultPageList(PageFactory.defaultPage(), param);
        this.format(userResultPage.getRecords());
        return userResultPage;
    }
    /**
     * 用户分页
     *
     * @param param
     * @return
     */
    public List<UserResult> userResultList(UserParam param) {
        List<UserResult> userResults = this.baseMapper.userResultList(param);
        this.format(userResults);
        return userResults;
    }

    public String getUserWechatOpenId(Long userId){
        User user = this.getById(userId);
        if (ToolUtil.isEmpty(user)){
            return "";
        }

        return this.baseMapper.getUserOpenId(userId);
    }
    public UserResult getUserResultByOpenId(String openId){

        if (ToolUtil.isEmpty(openId)){
            return new UserResult();
        }
        UserResult userResultByOpenId = this.baseMapper.getUserResultByOpenId(openId);
        if (ToolUtil.isEmpty(userResultByOpenId)){
            throw new ServiceException(500,"参数错误");
        }
        return userResultByOpenId;
    }
    public List<UserResult> getUserResultByOpenIds(List<String> openIds){

        if (ToolUtil.isEmpty(openIds) || openIds.size() == 0){
            return new ArrayList<>();
        }
        return this.baseMapper.getUserResultByOpenIds(openIds);
    }
    public User getByPhone(String phone){

        if (ToolUtil.isEmpty(phone)){
            return null;
        }
        return this.lambdaQuery().eq(User::getPhone,phone).one();
    }
}

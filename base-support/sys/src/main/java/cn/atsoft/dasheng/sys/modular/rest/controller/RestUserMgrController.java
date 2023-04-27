/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.model.response.ErrorResponseData;
import cn.atsoft.dasheng.sys.core.constant.Const;
import cn.atsoft.dasheng.sys.core.constant.dictmap.UserDict;
import cn.atsoft.dasheng.sys.core.constant.state.ManagerStatus;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.core.util.SaltUtil;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUser;
import cn.atsoft.dasheng.sys.modular.rest.model.UserQueryParam;
import cn.atsoft.dasheng.sys.modular.rest.model.params.UserRoleParam;
import cn.atsoft.dasheng.sys.modular.rest.model.params.UserStatus;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.RestUserSelectWrapper;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.UserDto;
import cn.atsoft.dasheng.sys.modular.system.model.params.ChangePwdParam;
import cn.atsoft.dasheng.sys.modular.system.warpper.UserWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午1:08:17
 */
@RestController
@RequestMapping("/rest/mgr")
public class RestUserMgrController extends BaseController {

    @Autowired
    private RestUserService restUserService;

    /**
     * 通过用户id获取用户的信息
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @RequestMapping("/getUserById")
    public ResponseData getUserById(@RequestParam("userId") Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Map<String, Object> user = restUserService.getUserInfo(userId);
        return new SuccessResponseData(user);
    }

    /**
     * 获取用户详情
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @RequestMapping("/getUserInfo")
    @Permission
    public SuccessResponseData getUserInfo(@RequestParam("userId") Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new RequestEmptyException();
        }
        this.restUserService.assertAuth(userId);
        return new SuccessResponseData(restUserService.getUserInfo(userId));
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    public SuccessResponseData changePwd(@RequestBody ChangePwdParam changePwdParam) {

        String oldPassword = changePwdParam.getOldPassword();
        String newPassword = changePwdParam.getNewPassword();

        if (ToolUtil.isOneEmpty(oldPassword, newPassword)) {
            throw new RequestEmptyException();
        }
        this.restUserService.changePwd(oldPassword, newPassword);
        return SUCCESS_TIP;
    }

    /**
     * 查询管理员列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @RequestMapping("/list")
    public Object list(@RequestBody UserQueryParam userQueryParam) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(userQueryParam.getTimeLimit())) {
            String[] split = userQueryParam.getTimeLimit().split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (LoginContextHolder.getContext().isAdmin()) {
            Page<Map<String, Object>> users = restUserService.selectUsers(null, userQueryParam.getName(), beginTime, endTime, userQueryParam.getDeptId());
            Page wrapped = new UserWrapper(users).wrap();
            return PageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            Page<Map<String, Object>> users = restUserService.selectUsers(dataScope, userQueryParam.getName(), beginTime, endTime, userQueryParam.getDeptId());
            Page wrapped = new UserWrapper(users).wrap();
            return PageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 添加管理员
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
    public ResponseData add(@RequestBody UserDto user) {
        this.restUserService.addUser(user);
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    public ResponseData edit(@RequestBody UserDto user) {
        this.restUserService.editUser(user);
        return SUCCESS_TIP;
    }


    /**
     * 修改管理员
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/synchronizeAvatar")
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    public ResponseData synchronizeAvatar(@RequestBody UserDto user) {
        this.restUserService.editUser(user);
        return SUCCESS_TIP;
    }

    /**
     * 删除管理员（逻辑删除）
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    public ResponseData delete(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.restUserService.deleteUser(userId);
        return SUCCESS_TIP;
    }

    /**
     * 重置管理员的密码
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
    public ResponseData reset(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.restUserService.assertAuth(userId);
        RestUser restUser = this.restUserService.getById(userId);
        restUser.setSalt(SaltUtil.getRandomSalt());
        restUser.setPassword(SaltUtil.md5Encrypt(ConstantsContext.getDefaultPassword(), restUser.getSalt()));
        this.restUserService.updateById(restUser);
        return SUCCESS_TIP;
    }

    /**
     * 冻结用户
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData freeze(@RequestBody UserStatus userStatus) {
        Long userId = userStatus.getUserId();
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        this.restUserService.assertAuth(userId);
        this.restUserService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData unfreeze(@RequestBody UserStatus userStatus) {
        Long userId = userStatus.getUserId();
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.restUserService.assertAuth(userId);
        this.restUserService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }
    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
    public ResponseData setRole(@RequestBody UserRoleParam userRoleParam) {

        Long userId = userRoleParam.getUserId();
        String roleIds = userRoleParam.getRoleIds();

        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        this.restUserService.assertAuth(userId);
        this.restUserService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }
    @RequestMapping("/getMyInfo")
    public ResponseData getMyInfo(){
        Map<String, Object> userIndexInfo = restUserService.getUserIndexInfo();
        if (userIndexInfo == null) {
            return new ErrorResponseData("用户信息不存在");
        }
        return new SuccessResponseData(userIndexInfo);
    }

    /**
     * 上传图片
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    public ResponseData upload(@RequestPart("file") MultipartFile picture) {

        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        try {
            String fileSavePath = ConstantsContext.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new SuccessResponseData(pictureName);
    }

    @RequestMapping(value = "/Select", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false)RestUser user) {
        QueryWrapper<RestUser> userQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(user)){
            if (ToolUtil.isNotEmpty(user.getDeptId())){
                userQueryWrapper.eq("dept_id",user.getDeptId());
            }
            if (ToolUtil.isNotEmpty(user.getName())){
                userQueryWrapper.like("name",user.getName());
            }
        }

        List<Map<String, Object>> list = this.restUserService.listMaps(userQueryWrapper);
        RestUserSelectWrapper restUserSelectWrapper = new RestUserSelectWrapper(list);
        List<Map<String, Object>> result = restUserSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}

package cn.atsoft.dasheng.form.controller;


import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.pojo.UserList;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.params.UserParam;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/formUser")
public class UserController extends BaseController {
    @Autowired
    private StepsService stepsService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService openUserInfoService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public ResponseData userList(@RequestBody(required = false) UserParam userParam) {
        if (ToolUtil.isEmpty(userParam)) {
            userParam = new UserParam();
        }
        return ResponseData.success(userService.userResultList(userParam));
    }
}

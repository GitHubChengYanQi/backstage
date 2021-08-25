package cn.atsoft.dasheng.UserInfo.controller;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


@RestController
@RequestMapping("/bind")
@Api(tags = "报修")
public class UserinfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public ResponseData getuser(@RequestBody GetUser user) {
        File getuser = userInfoService.getuser(user);

        return ResponseData.success(getuser);
    }
}



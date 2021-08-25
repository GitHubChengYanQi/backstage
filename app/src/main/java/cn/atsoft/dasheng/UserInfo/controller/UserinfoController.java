package cn.atsoft.dasheng.UserInfo.controller;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import org.apache.commons.codec.binary.Base64;
import org.postgresql.core.Encoding;
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
        byte[] getuser = userInfoService.getuser(user);
//    File file = new File();
        String base64String = Base64.encodeBase64String(getuser);
        return ResponseData.success(base64String);
    }


}



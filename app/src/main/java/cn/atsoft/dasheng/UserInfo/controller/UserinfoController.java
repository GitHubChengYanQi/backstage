package cn.atsoft.dasheng.UserInfo.controller;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import io.swagger.annotations.Api;
import org.apache.commons.codec.binary.Base64;
import org.postgresql.core.Encoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@RestController
@RequestMapping("/api")
@Api(tags = "报修")
public class UserinfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public ResponseData getuser(@RequestBody GetUser user) {
        byte[] getuser = userInfoService.getuser(user);
//        String base64String = Base64.encodeBase64String(getuser);
        return ResponseData.success(getuser);
    }


    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public ResponseData subscribe(String randStr) {
        String name = userInfoService.backUser(randStr);
        return ResponseData.success(name);
    }

    @RequestMapping(value = "/binding", method = RequestMethod.GET)
    public ResponseData binding(String randStr) {
        userInfoService.binding(randStr);
        return ResponseData.success();
    }
}



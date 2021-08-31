package cn.atsoft.dasheng.userInfo.controller;

import cn.atsoft.dasheng.userInfo.model.BackUser;
import cn.atsoft.dasheng.userInfo.model.GetKey;
import cn.atsoft.dasheng.userInfo.model.GetUser;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Api(tags = "报修")
public class UserinfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 返回二维码
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public ResponseData getuser(@RequestBody GetUser user) {
        byte[] getuser = userInfoService.getuser(user);
//        String base64String = Base64.encodeBase64String(getuser);
        return ResponseData.success(getuser);
    }

    /**
     * 返回绑定用户
     *
     * @param randStr
     * @return
     */
    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public ResponseData subscribe(@RequestParam("randStr") String randStr) {
        BackUser backUser = userInfoService.backUser(randStr);
        return ResponseData.success(backUser);
    }

    /**
     * 绑定
     *
     * @param getKey
     * @return
     */
    @RequestMapping(value = "/binding", method = RequestMethod.POST)
    public ResponseData binding(@RequestBody GetKey getKey) {
        userInfoService.binding(getKey);
        return ResponseData.success();
    }

}



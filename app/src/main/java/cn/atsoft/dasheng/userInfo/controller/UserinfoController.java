package cn.atsoft.dasheng.userInfo.controller;

import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.userInfo.model.BackUser;
import cn.atsoft.dasheng.userInfo.model.GetBind;
import cn.atsoft.dasheng.userInfo.model.GetKey;
import cn.atsoft.dasheng.userInfo.model.GetUser;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Api(tags = "报修")
public class UserinfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WxCpService wxCpService;

    /**
     * 返回二维码
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public ResponseData getuser(@RequestBody GetUser user) {
        String getuser = userInfoService.getuser(user);
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

    /**
     * 手动绑定
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/binds", method = RequestMethod.POST)
    public ResponseData binds(@RequestBody GetBind getBind) {
        userInfoService.binds(getBind);
        return ResponseData.success();
    }


    /**
     * 获取ticket
     */
    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public ResponseData getTicket(@RequestParam String url) throws WxErrorException {
        String[] split = url.split("#");
        WxJsapiSignature jsapiSignature = wxCpService.getWxCpClient().createJsapiSignature(split[0]);
        return ResponseData.success(jsapiSignature);
    }
}



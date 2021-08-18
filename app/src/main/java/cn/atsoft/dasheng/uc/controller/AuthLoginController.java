package cn.atsoft.dasheng.uc.controller;

import cn.atsoft.dasheng.uc.config.AppWxConfiguration;
import cn.atsoft.dasheng.uc.config.AppWxProperties;
import cn.atsoft.dasheng.uc.config.ShanyanConfiguration;
import cn.atsoft.dasheng.uc.config.Shanyanproperties;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.httpRequest.request.AppleRequest;
import cn.atsoft.dasheng.uc.httpRequest.request.ShanyanRequest;
import cn.atsoft.dasheng.uc.model.params.MiniAppLoginParam;
import cn.atsoft.dasheng.uc.model.params.MiniAppUserProfileParam;
import cn.atsoft.dasheng.uc.model.params.SmsCodeParam;
import cn.atsoft.dasheng.uc.service.UcMemberAuth;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.banner.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/login")
@Api(tags = "前台用户登录接口")
public class AuthLoginController extends BaseController {

    @Autowired
    private UcMemberAuth ucMemberAuth;

    @Autowired
    private AppWxConfiguration appWxConfiguration;

    @Autowired
    private ShanyanConfiguration shanyanConfiguration;

    @ApiOperation(value = "手机验证码登录", httpMethod = "POST")
    @RequestMapping("/phone")
    public ResponseData<String> phoneByCode(@RequestBody @Valid SmsCodeParam smsCodeParam) {
        String token = ucMemberAuth.loginByCode(smsCodeParam.getPhone(), smsCodeParam.getCode());
        return ResponseData.success(token);
    }

    /**
     * OAuth2.0
     *
     * @param source
     * @param response
     * @throws IOException
     */
    @RequestMapping("/oauth/{source}")
    @ApiOperation(value = "OAuth2.0发起授权接口", httpMethod = "GET")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {

    }

    /**
     * OAuth2.0 回调
     *
     * @param source
     * @param callback
     * @return
     */
    @RequestMapping("/callback/{source}")
    @ApiOperation(value = "OAuth2.0回调接口", httpMethod = "POST", notes = "{source}=AppWx(微信登录) shanyan-android,shanyan-ios(闪验),apple(Sign in With Apple)")
    public ResponseData<String> callback(@PathVariable("source") String source, AuthCallback callback, @RequestBody(required = false) AuthCallback postData) {

        ToolUtil.copyProperties(postData, callback);
        if (ToolUtil.isEmpty(callback.getCode())) {
            throw new ServiceException(500, "Code不能为空:" + source);
        }
        AuthRequest authRequest = null;

        switch (source) {
            case "AppWx":

                callback.setState("x");
                AppWxProperties appWxProperties = this.appWxConfiguration.getAppWxProperties();
                authRequest = new AuthWeChatOpenRequest(AuthConfig.builder()
                        .ignoreCheckState(true)
                        .clientId(appWxProperties.getAppId())
                        .clientSecret(appWxProperties.getSecret())
                        .redirectUri("http://")
                        .build());
                break;
            case "shanyan-android":
                Shanyanproperties.config androidConfig = this.shanyanConfiguration.getShanyanproperties().getAndroid();
                authRequest = new ShanyanRequest(AuthConfig.builder()
                        .ignoreCheckState(true)
                        .clientId(androidConfig.getAppid())
                        .clientSecret(androidConfig.getAppKey())
                        .redirectUri("http://")
                        .build());
                break;
            case "shanyan-ios":
                Shanyanproperties.config iosConfig = this.shanyanConfiguration.getShanyanproperties().getIos();
                authRequest = new ShanyanRequest(AuthConfig.builder()
                        .ignoreCheckState(true)
                        .clientId(iosConfig.getAppid())
                        .clientSecret(iosConfig.getAppKey())
                        .redirectUri("http://")
                        .build());
                break;
            case "apple":
                authRequest = new AppleRequest(AuthConfig.builder()
                        .ignoreCheckState(true)
                        .clientId("x")
                        .clientSecret("x")
                        .redirectUri("http://")
                        .build());
                break;
            default:
                throw new ServiceException(500, "暂不支持:" + source);
        }

        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        if (!authResponse.ok()) {
            throw new ServiceException(500, authResponse.getMsg());
        }
        AuthUser data = (AuthUser) authResponse.getData();
        UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
        ToolUtil.copyProperties(data, ucOpenUserInfo);
        String token = ucMemberAuth.login(ucOpenUserInfo);
        return ResponseData.success(token);
    }

//    @RequestMapping("/test")
//    public void test(){
//        UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
//        ucOpenUserInfo.setSource("WECHAT_OPEN");
//        ucOpenUserInfo.setUuid("okBe71T-KNbGmNxvc6bHtLQukdTI");
//        String token = ucMemberAuth.login(ucOpenUserInfo);
//    }

    /**
     * @return
     */
    @RequestMapping("/miniprogram/code2session")
    @ApiOperation(value = "小程序code2session", httpMethod = "POST")
    public ResponseData<String> wxMiniApp(@RequestBody @Valid MiniAppLoginParam miniAppLoginParam) throws WxErrorException {
        if(ToolUtil.isOneEmpty(miniAppLoginParam, miniAppLoginParam.getCode())){
            throw new ServiceException(500,"参数错误");
        }
        String token = ucMemberAuth.code2session(miniAppLoginParam);
//        String token = ucMemberAuth.getUserProfile(miniAppLoginParam, sessionKey);
        return ResponseData.success(token);
    }

    @RequestMapping("/miniprogram/loginByPhone")
    @ApiOperation(value = "小程序code2session", httpMethod = "POST")
    public ResponseData<String> loginByPhone(@RequestBody @Valid MiniAppUserProfileParam miniAppLoginParam) throws WxErrorException {
        if(ToolUtil.isOneEmpty(miniAppLoginParam, miniAppLoginParam.getIv(),miniAppLoginParam.getEncryptedData())){
            throw new ServiceException(500,"参数错误");
        }
        String token = ucMemberAuth.loginByPhone(miniAppLoginParam);
//        String token = ucMemberAuth.getUserProfile(miniAppLoginParam, sessionKey);
        return ResponseData.success(token);
    }

    @RequestMapping("/miniprogram/getUserProfile")
    @ApiOperation(value = "小程序提交用户加密信息及iv返回全新token", httpMethod = "POST")
    public ResponseData<String> userInfo(@RequestBody MiniAppUserProfileParam miniAppUserProfileParam){
        if(ToolUtil.isOneEmpty(miniAppUserProfileParam, miniAppUserProfileParam.getIv(),miniAppUserProfileParam.getEncryptedData())){
            throw new ServiceException(500,"参数错误");
        }
        String token = ucMemberAuth.getUserProfile(miniAppUserProfileParam);
        return ResponseData.success(token);
    }

    @RequestMapping("/refreshToken")
    @ApiOperation(value = "刷新token")
    public ResponseData refreshToken(){
        return ResponseData.success(ucMemberAuth.refreshToken());
    }
}

package cn.atsoft.dasheng.uc.controller;

import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.auth.exception.AuthException;
import cn.atsoft.dasheng.base.auth.exception.enums.AuthExceptionEnum;
import cn.atsoft.dasheng.base.auth.jwt.JwtTokenUtil;
import cn.atsoft.dasheng.base.auth.jwt.payload.JwtPayLoad;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import cn.atsoft.dasheng.sys.core.auth.AuthServiceImpl;
import cn.atsoft.dasheng.sys.core.auth.cache.SessionManager;
import cn.atsoft.dasheng.sys.core.auth.util.TokenUtil;
import cn.atsoft.dasheng.sys.core.exception.InvalidKaptchaException;
import cn.atsoft.dasheng.sys.modular.rest.model.params.LoginParam;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.config.AppWxConfiguration;
import cn.atsoft.dasheng.uc.config.AppWxProperties;
import cn.atsoft.dasheng.uc.config.ShanyanConfiguration;
import cn.atsoft.dasheng.uc.config.Shanyanproperties;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.httpRequest.request.AppleRequest;
import cn.atsoft.dasheng.uc.httpRequest.request.ShanyanRequest;
import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.jwt.UcJwtTokenUtil;
import cn.atsoft.dasheng.uc.model.params.MiniAppLoginParam;
import cn.atsoft.dasheng.uc.model.params.MiniAppUserProfileParam;
import cn.atsoft.dasheng.uc.model.params.SmsCodeParam;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.model.result.UcOpenUserInfoResult;
import cn.atsoft.dasheng.uc.service.UcMemberAuth;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.atsoft.dasheng.action.dict.InStockDictEnum.userId;
import static cn.atsoft.dasheng.uc.utils.UserUtils.getPayLoad;

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

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxuserInfoService wxuserInfoService;

    @Autowired
    private SessionManager sessionManager;

    protected static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @ApiOperation(value = "手机验证码登录", httpMethod = "POST")
    @RequestMapping("/phone")
    public ResponseData phoneByCode(@RequestBody @Valid SmsCodeParam smsCodeParam) {
        String token = ucMemberAuth.loginByCode(smsCodeParam.getPhone(), smsCodeParam.getCode());
        return ResponseData.success(token);
    }


    /**
     * OAuth2.0
     *
     * @param source
     * @throws IOException
     */
    @RequestMapping("/oauth/{source}")
    @ApiOperation(value = "OAuth2.0发起授权接口", httpMethod = "GET")
    public ResponseData renderAuth(@PathVariable("source") String source, @RequestParam(value = "url", required = true) String url) {
        switch (source) {
            case "wxMp":
                Map<String, Object> result = new HashMap<String, Object>() {
                    {
                        put("url", ucMemberAuth.buildAuthorizationUrl(url));
                    }
                };
                return ResponseData.success(result);
            case "wxCp":
                Map<String, Object> cpResult = new HashMap<String, Object>() {
                    {
                        put("url", ucMemberAuth.buildAuthori0zationUrlCp(url));
                    }
                };
                return ResponseData.success(cpResult);
            default:
                throw new ServiceException(500, "暂不支持:" + source);
        }
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
    public ResponseData callback(@PathVariable("source") String source, AuthCallback callback, @RequestBody(required = false) AuthCallback postData) {

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

    @RequestMapping("/mp/loginByCode")
    @ApiOperation(value = "公众号通过Code登录", httpMethod = "GET")
    public ResponseData mpLoginByCode(@RequestParam("code") String code) {
        String token = ucMemberAuth.mpLogin(code);
        return ResponseData.success(token);
    }


    @RequestMapping("/cp/loginByCode")
    @ApiOperation(value = "企业微信通过Code登录", httpMethod = "GET")
    public ResponseData cpLoginByCode(@RequestParam("code") String code) {
        String token = ucMemberAuth.cpLogin(code);
        UcJwtPayLoad jwtPayLoad = UcJwtTokenUtil.getJwtPayLoad(token);
        Long memberId = jwtPayLoad.getUserId();

        if (ToolUtil.isNotEmpty(memberId)) {
            QueryWrapper<WxuserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("member_id", memberId);
            queryWrapper.in("source", "wxCp");
            queryWrapper.isNotNull("user_id");
            queryWrapper.eq("display", 1);
            queryWrapper.last("limit 1");
            WxuserInfo wxuserInfo = wxuserInfoService.getOne(queryWrapper);
            if (ToolUtil.isNotEmpty(wxuserInfo)) {
                User user = userService.getById(wxuserInfo.getUserId());
                if (ToolUtil.isNotEmpty(user)) {
                    JwtPayLoad payLoad = new JwtPayLoad(user.getUserId(), user.getAccount(), "xxxx");
                    token = JwtTokenUtil.generateToken(payLoad);
                    User byId = userService.getById(wxuserInfo.getUserId());
                    //创建登录会话
                    sessionManager.createSession(token, authService.user(byId.getAccount()));
                }
            }
        }


        return ResponseData.success(token);
    }

    /**
     * @return
     */
    @RequestMapping("/miniprogram/code2session")
    @ApiOperation(value = "小程序code2session", httpMethod = "POST")
    public ResponseData wxMiniApp(@RequestBody @Valid MiniAppLoginParam miniAppLoginParam) throws WxErrorException {
        if (ToolUtil.isOneEmpty(miniAppLoginParam, miniAppLoginParam.getCode())) {
            throw new ServiceException(500, "参数错误");
        }
        String token = ucMemberAuth.code2session(miniAppLoginParam);

//        String token = ucMemberAuth.getUserProfile(miniAppLoginParam, sessionKey);
        return ResponseData.success(token);
    }

    @RequestMapping("/miniprogram/loginByPhone")
    @ApiOperation(value = "小程序code2session", httpMethod = "POST")
    public ResponseData loginByPhone(@RequestBody @Valid MiniAppUserProfileParam miniAppLoginParam) throws WxErrorException {
        if (ToolUtil.isOneEmpty(miniAppLoginParam, miniAppLoginParam.getIv(), miniAppLoginParam.getEncryptedData())) {
            throw new ServiceException(500, "参数错误");
        }
        String token = ucMemberAuth.loginByPhone(miniAppLoginParam);
//        String token = ucMemberAuth.getUserProfile(miniAppLoginParam, sessionKey);
        return ResponseData.success(token);
    }

    @RequestMapping("/miniprogram/getUserProfile")
    @ApiOperation(value = "小程序提交用户加密信息及iv返回全新token", httpMethod = "POST")
    public ResponseData userInfo(@RequestBody MiniAppUserProfileParam miniAppUserProfileParam) {
        if (ToolUtil.isOneEmpty(miniAppUserProfileParam, miniAppUserProfileParam.getIv(), miniAppUserProfileParam.getEncryptedData())) {
            throw new ServiceException(500, "参数错误");
        }
        String token = ucMemberAuth.getUserProfile(miniAppUserProfileParam);
        return ResponseData.success(token);
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/wxCp", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("登录接口")
    public ResponseData restLogin(@RequestBody @Valid LoginParam loginParam) {
        String username = loginParam.getUserName();
        String password = loginParam.getPassword();
        String kaptcha = loginParam.getKaptcha();  //验证码

        //验证验证码是否正确
        if (ConstantsContext.getKaptchaOpen()) {
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }
        try {
            UcJwtPayLoad ucJwtPayLoad = getPayLoad();
            String token = authService.login(username, password);
            JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);
            Long userId = jwtPayLoad.getUserId();//userId
            Long memberId = ucJwtPayLoad.getMemberId();//memberId
            if (ToolUtil.isNotEmpty(ucJwtPayLoad.getType())
//                    && ucJwtPayLoad.getType().equals("wxCp")
                   && ToolUtil.isNotEmpty(userId)) {
                WxuserInfo wxuserInfo = new WxuserInfo();
                wxuserInfo.setMemberId(memberId);
                wxuserInfo.setUserId(userId);
//                wxuserInfo.setSource("wxCp");
                QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
                wxuserInfoQueryWrapper.eq("user_id", userId);
//                wxuserInfoQueryWrapper.eq("source", ucJwtPayLoad.getType());
                wxuserInfoQueryWrapper.eq("display", 1);
                wxuserInfoService.saveOrUpdate(wxuserInfo, wxuserInfoQueryWrapper);
            }
            logger.info("account"+username+"_"+"userId"+userId+"_"+"ucJwtPayLoad"+ JSON.toJSONString(ucJwtPayLoad));
            return ResponseData.success(token);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        //登录并创建token
//        String token = authService.login(username, password);
        throw new AuthException(AuthExceptionEnum.USERNAME_PWD_ERROR);
    }
    /**
     * 企业微信退出 删除绑定关系
     */
    @RequestMapping(value = "/cpLogOut", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("企业微信退出")
    public ResponseData cpLogOut() {

        try {
            String token = TokenUtil.getToken();
            JwtPayLoad jwtPayLoad = JwtTokenUtil.getJwtPayLoad(token);
            Long userId = jwtPayLoad.getUserId();//userId
            if (ToolUtil.isNotEmpty(userId)) {
                WxuserInfo wxuserInfo = new WxuserInfo();
                wxuserInfo.setUserId(userId);
                wxuserInfo.setDisplay(0);
                QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
                wxuserInfoQueryWrapper.eq("user_id", userId);
                wxuserInfoQueryWrapper.eq("source", "wxCp");
                wxuserInfoQueryWrapper.eq("display", 1);
                wxuserInfoService.update(wxuserInfo, wxuserInfoQueryWrapper);
            }
            logger.info("userId: " + userId + " 退出登录");
            return ResponseData.success();
        } catch (Exception e) {

        }
        return ResponseData.error("退出失败");
    }


    @RequestMapping("/refreshToken")
    @ApiOperation(value = "刷新token")
    public ResponseData refreshToken() {
        return ResponseData.success(ucMemberAuth.refreshToken());
    }
}

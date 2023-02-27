package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.auth.jwt.payload.JwtPayLoad;
import cn.atsoft.dasheng.binding.cpUser.entity.CpuserInfo;
import cn.atsoft.dasheng.binding.cpUser.model.params.CpuserInfoParam;
import cn.atsoft.dasheng.binding.cpUser.service.CpuserInfoService;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.jwt.UcJwtTokenUtil;
import cn.atsoft.dasheng.uc.model.params.MiniAppLoginParam;
import cn.atsoft.dasheng.uc.model.params.MiniAppUserProfileParam;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.atsoft.dasheng.base.auth.jwt.JwtTokenUtil;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.core.log.LogManager;
import cn.atsoft.dasheng.sys.core.log.factory.LogTaskFactory;
import cn.atsoft.dasheng.sys.modular.system.entity.LoginLog;
import cn.atsoft.dasheng.sys.modular.system.service.LoginLogService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.Gender;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.mp.api.WxMpService;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static cn.atsoft.dasheng.base.auth.jwt.JwtTokenUtil.generateToken;
import static cn.atsoft.dasheng.base.consts.ConstantsContext.getJwtSecretExpireSec;
import static cn.atsoft.dasheng.base.consts.ConstantsContext.getTokenHeaderName;
import static cn.atsoft.dasheng.core.util.HttpContext.getIp;
import static cn.atsoft.dasheng.uc.jwt.UcJwtTokenUtil.getJwtPayLoad;

@Service
public class UcMemberAuth {

    @Autowired
    private UcSmsCodeService ucSmsCodeService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UcMemberService ucMemberService;

    @Autowired
    private UcOpenUserInfoService ucOpenUserInfoService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private WxuserInfoService wxuserInfoService;


    @Autowired
    private UserService userService;

    @Autowired
    private CpuserInfoService cpuserInfoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    final private String redisPreKey = "user-center-";


    /**
     * 构建Url
     *
     * @return String 授权地址
     */
    public String buildAuthorizationUrl(String url) {
        return wxMpService.getOAuth2Service().buildAuthorizationUrl(url, "snsapi_userinfo", "");
    }

    public String buildAuthori0zationUrlCp(String url) {

        return wxCpService.getWxCpClient().getOauth2Service().buildAuthorizationUrl(url, "snsapi_base", "a-zA-Z0-9");
    }


    public String mpLogin(String code) {

        try {
            WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo wxOAuth2UserInfo = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, null);

            UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
            ucOpenUserInfo.setUuid(wxOAuth2UserInfo.getOpenid());
            ucOpenUserInfo.setSource("wxMp");

            ucOpenUserInfo.setUsername(wxOAuth2UserInfo.getNickname());
            ucOpenUserInfo.setNickname(wxOAuth2UserInfo.getNickname());
            ucOpenUserInfo.setAvatar(wxOAuth2UserInfo.getHeadImgUrl());
            ucOpenUserInfo.setGender(wxOAuth2UserInfo.getSex());
            String raw = JSON.toJSONString(wxOAuth2UserInfo);// .toString();
            ucOpenUserInfo.setRawUserInfo(raw);
            ucOpenUserInfo.setLocation(wxOAuth2UserInfo.getCountry() + "-" + wxOAuth2UserInfo.getProvince() + "-" + wxOAuth2UserInfo.getCity());
            return login(ucOpenUserInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(500, e.getMessage());
        }
    }

    public String cpLogin(String code) {
        WxCpServiceImpl cpService = wxCpService.getWxCpClient();
        try {
            WxCpOauth2UserInfo wxCpOauth2UserInfo = cpService.getOauth2Service().getUserInfo(code);
            UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
            ucOpenUserInfo.setSource("wxCp");
            if (ToolUtil.isNotEmpty(wxCpOauth2UserInfo.getUserId())) {
                WxCpUser wxCpUser = cpService.getUserService().getById(wxCpOauth2UserInfo.getUserId());
                ucOpenUserInfo.setUuid(wxCpOauth2UserInfo.getUserId());
                ucOpenUserInfo.setUsername(wxCpUser.getName());
                ucOpenUserInfo.setNickname(wxCpUser.getName());
                ucOpenUserInfo.setAvatar(wxCpUser.getAvatar());
                ucOpenUserInfo.setGender(Integer.valueOf(wxCpUser.getGender().getCode()));
                String raw = JSON.toJSONString(wxCpOauth2UserInfo);
                ucOpenUserInfo.setRawUserInfo(raw);
                ucOpenUserInfo.setLocation(wxCpUser.getAddress());
            }else{
                String s = wxCpService.getWxCpClient().getUserService().openid2UserId(wxCpOauth2UserInfo.getOpenId());
                WxCpUser wxCpUser = wxCpService.getWxCpClient().getUserService().getById(s);
                ucOpenUserInfo.setUuid(s);
                ucOpenUserInfo.setAvatar(wxCpUser.getAvatar());
                ucOpenUserInfo.setUsername(wxCpUser.getName());
                ucOpenUserInfo.setNickname(wxCpUser.getAlias());
                ucOpenUserInfo.setLocation(wxCpUser.getAddress());
                ucOpenUserInfo.setMobile(wxCpUser.getMobile());
            }

            return login(ucOpenUserInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(500, e.getMessage());
        }
    }



    /**
     * 根据手机验证码登录
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return token token
     */
    public String loginByCode(String phone, String code) {

        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();

        String dateTime = ToolUtil.getCreateTimeBefore(600);
        queryWrapper.gt("create_time", dateTime);
        queryWrapper.eq("log_name", "登录日志");
        int count = loginLogService.count(queryWrapper);
        if (count >= 10) {
            LogManager.me().executeLog(LogTaskFactory.loginLog(phone, "登录错误次数过多:" + code, getIp()));
            throw new ServiceException(400, "登录错误次数过多");
        }
        QueryWrapper<UcSmsCode> codeQueryWrapper = new QueryWrapper<>();

        // 查找300秒内的验证码
        String codeDateTime = ToolUtil.getCreateTimeBefore(300);
        codeQueryWrapper.eq("mobile", phone);
        codeQueryWrapper.eq("code", code);
        codeQueryWrapper.gt("create_time", codeDateTime);


        UcSmsCode ucSmsCode = ucSmsCodeService.getOne(codeQueryWrapper);
        if (ToolUtil.isEmpty(ucSmsCode)) {
            //记录登录日志
            LogManager.me().executeLog(LogTaskFactory.loginLog(phone, "没有找到验证码:" + code, getIp()));
            throw new ServiceException(500, "验证码不正确");
        }


        return login(phone);
    }

    public String loginByPassword(String phone, String password) {
        return null;
    }


    public String loginByPhone(MiniAppUserProfileParam miniAppLoginParam) throws WxErrorException {

        String openId = UserUtils.getUserAccount();

        Object sessionKey = redisTemplate.boundValueOps(redisPreKey + "sessionKey").get();
        UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
        ucOpenUserInfo.setUuid(openId);
        ucOpenUserInfo.setSource("WXMINIAPP");

        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey.toString(), miniAppLoginParam.getEncryptedData(), miniAppLoginParam.getIv());

        String phoneNumber = wxMaPhoneNumberInfo.getPhoneNumber();
        if (ToolUtil.isEmpty(phoneNumber)) {
            throw new ServiceException(500, "数据错误");
        }
        Long id = addMember(phoneNumber);


        ucOpenUserInfo.setMemberId(id);
        return login(ucOpenUserInfo);
    }

    // TODO unionId 未做处理
    public String code2session(MiniAppLoginParam miniAppLoginParam) throws WxErrorException {
        String code = miniAppLoginParam.getCode();
        WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(code);
        AuthUser userInfo = new AuthUser();
        String sessionKey = result.getSessionKey();

        // TODO 多台服务器负载的情况下应 使用 Redis 保存Session spring-session-data-redis
//        HttpSession session = Objects.requireNonNull(HttpContext.getRequest()).getSession();
//        session.setAttribute("wxMiniApp-sessionKey", sessionKey); // 解密 小程序提交的 加密用户信息
        redisTemplate.boundValueOps(redisPreKey + "sessionKey").set(sessionKey);
        userInfo.setUuid(result.getOpenid());
        userInfo.setSource("WXMINIAPP");
        UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
        ToolUtil.copyProperties(userInfo, ucOpenUserInfo);

        String token = login(ucOpenUserInfo);

        try{
            UcJwtPayLoad jwtPayLoad = getJwtPayLoad(token);
            Long memberId = jwtPayLoad.getMemberId();
            WxuserInfo wxuserInfo = wxuserInfoService.getByMemberId(memberId);
            if(ToolUtil.isEmpty(wxuserInfo) || ToolUtil.isEmpty(wxuserInfo.getUserId())){
                return token;
            }
            User user = userService.getById(wxuserInfo.getUserId());
            if(ToolUtil.isEmpty(user)){
                return token;
            }

            JwtPayLoad    payLoad = new JwtPayLoad(wxuserInfo.getUserId(), user.getAccount(), "xxxx");

            return JwtTokenUtil.generateToken(payLoad);
        }catch (Exception e){
                e.printStackTrace();
        }
        return token;

    }

    public String getUserProfile(MiniAppUserProfileParam miniAppUserProfileParam) {

        String openId = UserUtils.getUserAccount();
        Object sessionKey = redisTemplate.boundValueOps(redisPreKey + "sessionKey").get();
        WxMaUserInfo result = wxMaService.getUserService().getUserInfo(sessionKey.toString(), miniAppUserProfileParam.getEncryptedData(), miniAppUserProfileParam.getIv());
        UcOpenUserInfo ucOpenUserInfo = new UcOpenUserInfo();
        ucOpenUserInfo.setUuid(openId);
        ucOpenUserInfo.setSource("WXMINIAPP");

        ucOpenUserInfo.setUsername(result.getNickName());
        ucOpenUserInfo.setNickname(result.getNickName());
        ucOpenUserInfo.setAvatar(result.getAvatarUrl());
        String genderStr = result.getGender();
//        AuthUserGender gender = AuthUserGender.getRealGender(genderStr);
        ucOpenUserInfo.setGender(Integer.valueOf(genderStr));
        String raw = JSON.toJSONString(result);// .toString();
        ucOpenUserInfo.setRawUserInfo(raw);
        ucOpenUserInfo.setLocation(result.getCountry() + "-" + result.getProvince() + "-" + result.getCity());


//        ToolUtil.copyProperties(userInfo, ucOpenUserInfo);

        return login(ucOpenUserInfo);
    }

    private Long addMember(String phone) {

        Long id = 0L;
        UcMember ucMember = new UcMember();
        ucMember.setPhone(phone);

        QueryWrapper<UcMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", ucMember.getPhone());
        UcMember result = ucMemberService.getOne(queryWrapper);

        if (ToolUtil.isEmpty(result)) {
            // 找不到的情况下，在用户表中创建一条信息
            ucMemberService.getBaseMapper().insert(ucMember);
            id = ucMember.getMemberId();
        } else {
            id = result.getMemberId();
        }

        return id;
    }

    private UcOpenUserInfo addOpenMember(UcOpenUserInfo userInfo) {
        String primaryKey = userInfo.getSource() + "-" + userInfo.getUuid();
        UcOpenUserInfo ucOpenUserInfo = ucOpenUserInfoService.getById(primaryKey);
        if (ToolUtil.isEmpty(ucOpenUserInfo)) {
            UcOpenUserInfoParam ucOpenUserInfoParam = new UcOpenUserInfoParam();
            ToolUtil.copyProperties(userInfo, ucOpenUserInfoParam);
            ucOpenUserInfoParam.setPrimaryKey(primaryKey);
            return ucOpenUserInfoService.add(ucOpenUserInfoParam);
        } else {
            ToolUtil.copyProperties(userInfo, ucOpenUserInfo);
//            userInfo.setPrimaryKey(primaryKey);
            ucOpenUserInfoService.updateById(ucOpenUserInfo);
            return ucOpenUserInfo;
        }
    }

    /**
     * 构建登录token,返回包含用户信息的token
     *
     * @param phone 手机号码
     * @return token 返回toekn
     */
    public String login(String phone) {

        UcOpenUserInfo authUser = new UcOpenUserInfo();
        authUser.setSource("SMSCODE");
        authUser.setUuid(phone);
        authUser.setUsername(phone);
        authUser.setNickname(phone);

        return login(authUser);
    }

    /**
     * 构建登录token,返回包含用户信息的token
     *
     * @param userInfo 第三方用户信息
     * @return token 返回toekn
     */
    public String login(UcOpenUserInfo userInfo) {

        Long memberId = 0L;

        /**
         * 如果是手机方式登录先创建member数据
         */
        String account = userInfo.getUuid();
        if (userInfo.getSource().equals("SHANYAN") || userInfo.getSource().equals("SMSCODE")) {
            memberId = addMember(account);
            userInfo.setMemberId(memberId);
        }

        /**
         * 保存到第三方登录信息
         */
        UcOpenUserInfo ucOpenUserInfo = addOpenMember(userInfo);
        memberId = ucOpenUserInfo.getMemberId();
        /**
         * 当前已登录用户信息
         */
        UcJwtPayLoad ucJwtPayLoad = UserUtils.getPayLoad();
        String loginedType = ucJwtPayLoad.getType();
        String loginedAccount = ucJwtPayLoad.getAccount();

        /**
         * 当前已登录，并且与新的登录方式不同，以下是自动绑定逻辑
         */
        if (ToolUtil.isNotEmpty(loginedType) && !loginedType.equals(userInfo.getSource())) {
            String primaryKey = loginedType + "-" + loginedAccount;
            UcOpenUserInfo updateUcOpenUserInfo = ucOpenUserInfoService.getById(primaryKey);


            /**
             * 新登录用户没有memberId，并且当前的有memberId（先是手机登录，后用微信等方式登录）
             * 这种情况给memberId赋值
             */
            if (ToolUtil.isNotEmpty(updateUcOpenUserInfo.getMemberId()) && memberId <= 0) {
                memberId = updateUcOpenUserInfo.getMemberId();
                ucOpenUserInfo.setMemberId(memberId);
                ucOpenUserInfoService.updateById(ucOpenUserInfo);
            }
            /**
             * 新登录用户信息有memberId 并且当前登录信息没有 memberId（先是微信等登录方式，后用手机登录方式会是这种情况）
             * 这种情况memberId直接更新当前数据
             */
            if (ToolUtil.isNotEmpty(updateUcOpenUserInfo) && ToolUtil.isNotEmpty(memberId)) {
                updateUcOpenUserInfo.setMemberId(memberId);
                ucOpenUserInfoService.updateById(updateUcOpenUserInfo);
            }
        }

        String mobile = null;

        if (ToolUtil.isNotEmpty(memberId)) {
            QueryWrapper<UcMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("member_id", memberId);
            UcMember ucMember = ucMemberService.getOne(queryWrapper);
            if (ToolUtil.isNotEmpty(ucMember)) {
                mobile = ucMember.getPhone();
            }
        }

        UcJwtPayLoad payLoad = new UcJwtPayLoad(userInfo.getSource(), null, account, "xxxx");

        payLoad.setType(userInfo.getSource());
        payLoad.setMobile(mobile);
        payLoad.setMemberId(memberId);
        String token = generateToken(payLoad);
        // TODO 可以用 sessionManage缓存用户信息，暂时先不加了

        // 创建cookie
        addLoginCookie(token);

        return token;



    }


    public String refreshToken() {
        String type = UserUtils.getType();
        String account = UserUtils.getUserAccount();
        Long id = UserUtils.getUserId();
        String mobile = UserUtils.getMobile();

        UcJwtPayLoad payLoad = new UcJwtPayLoad(type, id, account, "xxxx");
//        payLoad.setType(type);
        payLoad.setMobile(mobile);
        String token = generateToken(payLoad);
        // 创建cookie
        addLoginCookie(token);

        return token;
    }

    public void addLoginCookie(String token) {
        // 创建cookie
        Cookie authorization = new Cookie(getTokenHeaderName(), token);
        authorization.setMaxAge(getJwtSecretExpireSec().intValue());
        authorization.setHttpOnly(true);
        authorization.setPath("/");
        HttpServletResponse response = HttpContext.getResponse();
        response.addCookie(authorization);
    }
}

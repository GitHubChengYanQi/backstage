package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.model.params.MiniAppLoginParam;
import cn.atsoft.dasheng.uc.model.params.MiniAppUserProfileParam;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.atsoft.dasheng.base.auth.jwt.JwtTokenUtil;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.banner.model.exception.ServiceException;
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
import me.chanjar.weixin.common.error.WxErrorException;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static cn.atsoft.dasheng.base.consts.ConstantsContext.getJwtSecretExpireSec;
import static cn.atsoft.dasheng.base.consts.ConstantsContext.getTokenHeaderName;
import static cn.atsoft.dasheng.core.util.HttpContext.getIp;

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
    private RedisTemplate<String, Object> redisTemplate;

    final private String redisPreKey = "user-center-";

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
        queryWrapper.eq("phone", phone);
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

        return login(ucOpenUserInfo);
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

    private void addOpenMember(UcOpenUserInfo userInfo) {
        String primaryKey = userInfo.getSource() + "-" + userInfo.getUuid();
        UcOpenUserInfo ucOpenUserInfo = ucOpenUserInfoService.getById(primaryKey);
        if (ToolUtil.isEmpty(ucOpenUserInfo)) {
            ucOpenUserInfo = new UcOpenUserInfo();
            ToolUtil.copyProperties(userInfo, ucOpenUserInfo);
            ucOpenUserInfo.setPrimaryKey(primaryKey);
            ucOpenUserInfoService.getBaseMapper().insert(ucOpenUserInfo);
        } else {
            UcOpenUserInfoParam ucOpenUserInfoParam = new UcOpenUserInfoParam();
            ToolUtil.copyProperties(userInfo, ucOpenUserInfoParam);
            ucOpenUserInfoParam.setPrimaryKey(primaryKey);
            ucOpenUserInfoService.update(ucOpenUserInfoParam);
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
        addOpenMember(userInfo);

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
            UcOpenUserInfo ucOpenUserInfo = ucOpenUserInfoService.getById(primaryKey);


            /**
             * 新登录用户没有memberId，并且当前的有memberId（先是手机登录，后用微信等方式登录）
             * 这种情况给memberId赋值
             */
            if (ToolUtil.isNotEmpty(ucOpenUserInfo.getMemberId()) && memberId <= 0) {
                memberId = ucOpenUserInfo.getMemberId();
            }
            /**
             * 新登录用户信息有memberId 并且当前登录信息没有 memberId（先是微信等登录方式，后用手机登录方式会是这种情况）
             * 这种情况memberId直接更新当前数据
             */
            if (ToolUtil.isNotEmpty(ucOpenUserInfo) && ToolUtil.isNotEmpty(memberId)) {
                ucOpenUserInfo.setMemberId(memberId);
                ucOpenUserInfoService.updateById(ucOpenUserInfo);
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

//        JwtPayLoad payLoad = new JwtPayLoad(memberId, account, "xxxx");
        UcJwtPayLoad payLoad = new UcJwtPayLoad(userInfo.getSource(),memberId, account, "xxxx");
//        payLoad.setType(userInfo.getSource());
        payLoad.setMobile(mobile);
        String token = JwtTokenUtil.generateToken(payLoad);
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

        UcJwtPayLoad payLoad = new UcJwtPayLoad(type,id, account, "xxxx");
//        payLoad.setType(type);
        payLoad.setMobile(mobile);
        String token = JwtTokenUtil.generateToken(payLoad);
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

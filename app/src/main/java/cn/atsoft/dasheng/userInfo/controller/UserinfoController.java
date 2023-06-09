package cn.atsoft.dasheng.userInfo.controller;

import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.UserDto;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcMemberService;
import cn.atsoft.dasheng.userInfo.model.BackUser;
import cn.atsoft.dasheng.userInfo.model.GetBind;
import cn.atsoft.dasheng.userInfo.model.GetKey;
import cn.atsoft.dasheng.userInfo.model.GetUser;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class UserinfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WxCpService wxCpService;;
    @Autowired
    private UserService userService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcMemberService memberService;
    @Autowired
    private OpenUserInfoService openUserInfoService;
    @Autowired
    private AuthService authService;

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
    @Transactional
    @RequestMapping(value = "/synchronizeAvatar", method = RequestMethod.POST)

    public ResponseData synchronizeAvatar(@RequestBody UserDto user) {
        Long userId = user.getUserId();
        HttpServletRequest request = HttpContext.getRequest();
        String appid = request.getParameter("appid");
        List<WxuserInfo> userInfoList = wxuserInfoService.lambdaQuery().eq(WxuserInfo::getUserId, userId).eq(WxuserInfo::getDisplay, 1).list();
        //取出userInfoList种的memberIds
        List<Long> memberIds = userInfoList.stream().map(WxuserInfo::getMemberId).distinct().collect(Collectors.toList());
        user.setPhone(null);
        String avatar = user.getAvatar();
        String nickName = user.getNickName();
        user.setAvatar(null);
        Long tenantId = LoginContextHolder.getContext().getTenantId();
        List<OpenUserInfo> list =memberIds.size() == 0 ? new ArrayList<>() : openUserInfoService.lambdaQuery().eq(OpenUserInfo::getAppid,appid).in(OpenUserInfo::getMemberId, memberIds).eq(OpenUserInfo::getSource,"WXMINIAPP").list();
        for (OpenUserInfo openUserInfo : list) {
            openUserInfo.setAvatar(avatar);
            openUserInfo.setNickname(nickName);
        }
        openUserInfoService.updateBatchById(list);
        User user1 = BeanUtil.copyProperties(user, User.class);
        userService.updateById(user1);
        return ResponseData.success( authService.login(LoginContextHolder.getContext().getUser().getAccount()));


//        String avatar = user.getAvatar();
//        user.setAvatar(null);
//        userInfoService
//        if (user == null) {
//            throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
//        }
//        user.setAvatar(avatar);
//        this.updateById(user);
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



package cn.stylefeng.guns.oauth.modular.service.impl;

import cn.stylefeng.guns.base.shiro.ShiroUser;
import cn.stylefeng.guns.oauth.core.exception.OAuthExceptionEnum;
import cn.stylefeng.guns.oauth.core.exception.OAuthLoginException;
import cn.stylefeng.guns.oauth.core.shiro.OAuthToken;
import cn.stylefeng.guns.oauth.modular.entity.OauthUserInfo;
import cn.stylefeng.guns.oauth.modular.factory.OAuthUserInfoFactory;
import cn.stylefeng.guns.oauth.modular.service.LoginService;
import cn.stylefeng.guns.oauth.modular.service.OauthUserInfoService;
import cn.stylefeng.guns.sys.core.shiro.ShiroKit;
import cn.stylefeng.guns.sys.modular.system.entity.User;
import cn.stylefeng.guns.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认第三方登录逻辑
 *
 * @author fengshuonan
 * @Date 2019/6/9 18:16
 */
@Service
public class DefaultLoginService implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private OauthUserInfoService oauthUserInfoService;

    @Override
    public String oauthLogin(AuthUser oauthUser) {

        if (oauthUser == null) {
            throw new OAuthLoginException(OAuthExceptionEnum.OAUTH_RESPONSE_ERROR);
        }

        //当前有登录用户
        if (ShiroKit.isUser()) {

            //当前登录用户
            ShiroUser user = ShiroKit.getUserNotNull();

            //绑定用户相关的openId
            bindOAuthUser(user.getId(), oauthUser);

            return "redirect:/system/user_info";

        } else {

            //当前无登录用户，则新创建登录用户
            createOAuthUser(oauthUser);

            //执行shiro的登录逻辑
            OAuthToken token = new OAuthToken(oauthUser.getUsername());
            ShiroKit.getSubject().login(token);

            return "redirect:/";
        }

    }


    /**
     * 绑定当前用户的source和openId
     *
     * @author fengshuonan
     * @Date 2019/6/9 18:51
     */
    private void bindOAuthUser(Long userId, AuthUser oauthUser) {

        //先判断当前系统这个openId有没有人用
        QueryWrapper<OauthUserInfo> queryWrapper = new QueryWrapper<OauthUserInfo>()
                .eq("source", oauthUser.getSource().name())
                .and(i -> i.eq("uuid", oauthUser.getUuid()))
                .and(i -> i.ne("user_id", userId));
        List<OauthUserInfo> oauthUserInfos = this.oauthUserInfoService.list(queryWrapper);

        //已有人绑定，抛出异常
        if (oauthUserInfos != null && oauthUserInfos.size() > 0) {
            throw new OAuthLoginException(OAuthExceptionEnum.OPEN_ID_ALREADY_BIND);
        }

        //新建一条绑定记录
        OauthUserInfo oAuthUserInfo = OAuthUserInfoFactory.createOAuthUserInfo(userId, oauthUser);
        this.oauthUserInfoService.save(oAuthUserInfo);

    }

    /**
     * 通过第三方登录的信息创建本系统用户
     *
     * @author fengshuonan
     * @Date 2019/6/9 19:07
     */
    private void createOAuthUser(AuthUser oauthUser) {

        // 判断账号是否重复
        User theUser = this.userService.getByAccount(oauthUser.getUsername());
        if (theUser != null) {
            return;
        }

        //创建用户
        User user = OAuthUserInfoFactory.createOAuthUser(oauthUser);
        this.userService.save(user);

        //创建第三方绑定信息
        this.bindOAuthUser(user.getUserId(), oauthUser);

    }

}
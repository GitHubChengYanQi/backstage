package cn.stylefeng.guns.oauth.modular.controller;

import cn.stylefeng.guns.oauth.modular.service.LoginService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.stylefeng.guns.oauth.modular.factory.OAuthRequestFactory.getAuthRequest;

/**
 * OAuth统一回调地址
 *
 * @author fengshuonan
 * @Date 2019/6/9 16:38
 */
@Controller
@RequestMapping("/oauth")
@Slf4j
public class OAuthController extends BaseController {

    @Autowired
    private LoginService loginService;

    /**
     * 第三方登录跳转
     * njui7crdxe
     *
     * @author fengshuonan
     * @Date 2019/6/9 16:44
     */
    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        response.sendRedirect(authRequest.authorize());
    }

    /**
     * 第三方登录成功后的回调地址
     *
     * @author fengshuonan
     * @Date 2019/6/9 16:45
     */
    @RequestMapping("/callback/{source}")
    public String callback(@PathVariable("source") String source, @RequestParam("code") String code, RedirectAttributes model) {

        //通过回调的code，请求对应的oauth server获取用户基本信息和token
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse authResponse = authRequest.login(code);

        AuthUser oauthUser = (AuthUser) authResponse.getData();
        log.info("第三方登录回调成功：" + oauthUser);

        //进行第三方用户登录过程
        loginService.oauthLogin(oauthUser);

        return "redirect:/";
    }

}
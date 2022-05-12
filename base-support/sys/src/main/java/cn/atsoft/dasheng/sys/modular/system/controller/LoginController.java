/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.sys.core.auth.cache.SessionManager;
import cn.atsoft.dasheng.sys.core.exception.InvalidKaptchaException;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.tenant.context.DataBaseNameHolder;
import cn.atsoft.dasheng.base.tenant.context.TenantCodeHolder;
import cn.atsoft.dasheng.base.tenant.entity.TenantInfo;
import cn.atsoft.dasheng.base.tenant.service.TenantInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.entity.DictType;
import cn.atsoft.dasheng.sys.modular.system.service.DictService;
import cn.atsoft.dasheng.sys.modular.system.service.DictTypeService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.mutidatasource.DataSourceContextHolder;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import com.google.code.kaptcha.Constants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author fengshuonan
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private DictService dictService;

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 跳转到主页
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        //判断用户是否登录
        if (LoginContextHolder.getContext().hasLogin()) {
            Map<String, Object> userIndexInfo = userService.getUserIndexInfo();

            //用户信息为空，提示账号没分配角色登录不进去
            if (userIndexInfo == null) {
                model.addAttribute("tips", "该用户没有角色，无法登陆");
                return "/login.html";
            } else {
                model.addAllAttributes(userIndexInfo);
                return "/index.html";
            }

        } else {
            return "/login.html";
        }
    }

    /**
     * 跳转到登录页面
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (LoginContextHolder.getContext().hasLogin()) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData loginVali(HttpServletRequest request, HttpServletResponse response) {

        String username = super.getPara("username");
        String password = super.getPara("password");

        if (ToolUtil.isOneEmpty(username, password)) {
            throw new RequestEmptyException("账号或密码为空！");
        }

        //如果系统开启了多租户开关，则添加租户的临时缓存
        if (ConstantsContext.getTenantOpen()) {
            String tenantCode = super.getPara("tenantCode");
            if (ToolUtil.isNotEmpty(tenantCode)) {

                //从spring容器中获取service，如果没开多租户功能，没引入相关包，这里会报错
                TenantInfoService tenantInfoService = null;
                try {
                    tenantInfoService = SpringContextHolder.getBean(TenantInfoService.class);
                } catch (Exception e) {
                    throw new ServiceException(500, "找不到多租户service，请检查是否引入guns-tenant模块！");
                }

                //获取租户信息
                TenantInfo tenantInfo = tenantInfoService.getByCode(tenantCode);
                if (tenantInfo != null) {
                    String dbName = tenantInfo.getDbName();

                    //添加临时变量（注意销毁）
                    TenantCodeHolder.put(tenantCode);
                    DataBaseNameHolder.put(dbName);
                    DataSourceContextHolder.setDataSourceType(dbName);
                } else {
                    throw new ServiceException(BizExceptionEnum.NO_TENANT_ERROR);
                }
            }
        }

        //验证验证码是否正确
        if (ConstantsContext.getKaptchaOpen()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        //登录并创建token
        String token = authService.login(username, password);

        return new SuccessResponseData(token);
    }

    /**
     * 利用已有的token进行登录（一般用在oauth登录）
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/sysTokenLogin")
    public String sysTokenLogin(@RequestParam(value = "token", required = false) String token, Model model) {
        if (ToolUtil.isNotEmpty(token)) {

            //从session获取用户信息，没有就是token无效
            LoginUser user = sessionManager.getSession(token);
            if (user == null) {
                return "/login.html";
            }

            //创建当前登录上下文
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //渲染首页需要的用户信息
            Map<String, Object> userIndexInfo = userService.getUserIndexInfo();
            if (userIndexInfo == null) {
                model.addAttribute("tips", "该用户没有角色，无法登陆！");
                return "/login.html";
            } else {
                model.addAllAttributes(userIndexInfo);
            }

            //创建cookie
            authService.addLoginCookie(token);

            return "/index.html";
        } else {
            model.addAttribute("tips", "token请求为空，无法登录！");
            return "/login.html";
        }
    }

    /**
     * 验证码是否开启
     * @return
     */
    @RequestMapping(value = "/getKaptchaOpen")
    @ResponseBody
    public ResponseData getKaptchaOpen() {
        Map<String, String> map = new HashMap<>();
        Boolean aBoolean = ConstantsContext.getKaptchaOpen();
        map.put("kaptchaOpen", aBoolean.toString());
        DictType dictType = dictTypeService.query().eq("code", "SYSTEM_INFO").one();

        List<Dict> systemInfos = ToolUtil.isEmpty(dictType) ? new ArrayList<>() :  dictService.listDicts(dictType.getDictTypeId());
        if (ToolUtil.isNotEmpty(systemInfos)){
            for (Dict systemInfo : systemInfos) {
                map.put(systemInfo.getCode(),systemInfo.getName());
            }

        }


        return ResponseData.success(map);
    }


    /**
     * 退出登录
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public ResponseData logOut() {
        authService.logout();
        return new SuccessResponseData();
    }


}
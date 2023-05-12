package cn.atsoft.dasheng.sys.core.auth.filter;

/**
 * 不需要身份验证的资源
 */
public class NoneAuthedResources {

    /**
     * 前端接口资源
     */
    public static final String[] FRONTEND_RESOURCES = {
            "/assets/**",
            "/favicon.ico",
            "/activiti-editor/**"
    };


    /**
     * 不要权限校验的后端接口资源
     * <p>
     * ANT风格的接口正则表达式：
     * <p>
     * ? 匹配任何单字符<br/>
     * * 匹配0或者任意数量的 字符<br/>
     * ** 匹配0或者更多的 目录<br/>
     */
    public static final String[] BACKEND_RESOURCES = {

            "/druid/**",
            //微信回调
            "/wx/**",

            //主页
            "/",

            // 锁屏
            "/system/lock",

            //获取验证码
            "/kaptcha",

            //rest方式获取token入口
            "/rest/login",

            //系统公共信息
            "/getPublicInfo",
            //获取登录信息
            "/rest/mgr/getMyInfo",

            //消息跳转接口
            "/message/jump",

            //oauth登录的接口
            "/oauth/render/*",
            "/oauth/callback/*",

            //单点登录接口
            "/ssoLogin",
            "/sysTokenLogin",

            // 登录接口放开过滤
            "/login/**",
            "/api/**",
            "/sms/sendCode",
            "/pay/**",
            "crm/excel/**",

            // session登录失效之后的跳转
            "/global/sessionError",

            // 图片预览 头像
            "/system/preview/*",

            // 错误页面的接口
            "/error",
            "/global/error",

            // 测试多数据源的接口，可以去掉
            "/tran/**",

            //获取租户列表的接口
            "/tenantInfo/listTenants",


            "/tenant/detail",
            "/tenantBindLog/detail",
            "/tenantInviteLog/detail",

            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/wxmp/messageCallBack",
            "/wxma/messageCallBack/*",
            "/wxma/messageCallBack/**",
            "/v2/**",
            //小程序菜单
            "/mobelTableView/miniappViewDetail"

    };

}

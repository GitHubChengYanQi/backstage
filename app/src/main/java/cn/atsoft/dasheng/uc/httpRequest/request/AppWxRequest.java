package cn.atsoft.dasheng.uc.httpRequest.request;

import cn.atsoft.dasheng.uc.config.AppWxConfiguration;
import cn.atsoft.dasheng.uc.httpRequest.source.AppWxSource;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.config.HttpConfig;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.HttpUtils;
import me.zhyd.oauth.utils.UrlBuilder;

public class AppWxRequest extends AuthDefaultRequest {

//    @Autowired
//    private AppWxConfiguration appWxConfiguration;

    private String appid;

    public AppWxRequest(AuthConfig config) {
        super(config, AppWxSource.APPWX);
    }

    public AppWxRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AppWxSource.APPWX, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
//        String response = doPostAuthorizationCode(authCallback.getCode());
        String response = new HttpUtils(HttpConfig.builder().build()).get(accessTokenUrl(authCallback.getCode()));
        JSONObject object = JSONObject.parseObject(response);

        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        return null;
    }

    public String accessTokenUrl(String code) {
        AppWxConfiguration appWxConfiguration = SpringContextHolder.getBean(AppWxConfiguration.class);
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .queryParam("appid", appWxConfiguration.getAppWxProperties().getAppId())
                .queryParam("secret", appWxConfiguration.getAppWxProperties().getSecret())
                .build();
    }
}

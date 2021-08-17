package cn.atsoft.dasheng.uc.httpRequest.request;

import cn.atsoft.dasheng.uc.httpRequest.source.AppleSource;
import cn.atsoft.dasheng.uc.model.params.AppleIdentity;
import cn.atsoft.dasheng.uc.model.params.AppleParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.model.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.config.HttpConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.HttpUtils;
import me.zhyd.oauth.utils.UrlBuilder;
import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

public class AppleRequest extends AuthDefaultRequest {

    private AuthUser authUser = new AuthUser();

    public AppleRequest(AuthConfig config) {
        super(config, AppleSource.APPLE_SOURCE);
    }

    public AppleRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AppleSource.APPLE_SOURCE, authStateCache);
    }

    @SneakyThrows
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {

        if(ToolUtil.isEmpty(authCallback.getAuth_code())){
            throw new ServiceException(500, "AuthCode错误");
        }
        String[] identityTokens = authCallback.getCode().split("\\.");

        if (identityTokens.length != 3) {
            throw new ServiceException(500, "CODE错误");
        }
        String response = new HttpUtils(HttpConfig.builder().build()).get(accessTokenUrl());
        AppleParam object = JSONObject.parseObject(response, AppleParam.class);

        if (null != object) {
            List<AppleParam.key> keys = object.getKeys();
            if (keys != null) {


                JSONObject firstData = JSON.parseObject(new String(Base64.decodeBase64(identityTokens[0]), "UTF-8"));
                AppleIdentity data = JSON.parseObject(new String(Base64.decodeBase64(identityTokens[1]), "UTF-8"), AppleIdentity.class);

                String kid = firstData.getString("kid");
                for (AppleParam.key key : keys) {
                    if (key.getKid().equals(kid)) {
                        String n = key.getN();
                        String e = key.getE();

                        final BigInteger modulus = new BigInteger(1, Base64.decodeBase64(n));
                        final BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(e));
                        final RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
                        final KeyFactory kf = KeyFactory.getInstance("RSA");
                        PublicKey publicKey = kf.generatePublic(spec);
                        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
                        jwtParser.requireIssuer(data.getIss());
                        jwtParser.requireAudience(data.getAud());
                        jwtParser.requireSubject(data.getSub());
                        Jws<Claims> claim = jwtParser.parseClaimsJws(authCallback.getCode());
                        if (null != claim && claim.getBody().containsKey("auth_time")) {
//                            return true;
                            this.authUser.setSource("APPLE");
                            this.authUser.setUuid(authCallback.getAuth_code());
                            this.authUser.setUsername(data.getEmail());
                            this.authUser.setNickname(data.getEmail());
                        }
                        return AuthToken.builder()
                                .accessToken("")
                                .build();
                    }
                }
            }
        }
        throw new ServiceException(500, "Sign in With Apple Error");
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {

        return this.authUser;
    }

    public String accessTokenUrl() {
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .build();
    }
}

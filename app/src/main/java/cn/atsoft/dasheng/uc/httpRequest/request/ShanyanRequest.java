package cn.atsoft.dasheng.uc.httpRequest.request;

import cn.atsoft.dasheng.uc.httpRequest.source.ShanyanSource;
import cn.atsoft.dasheng.uc.utils.AESUtils;
import cn.atsoft.dasheng.uc.utils.ByteFormat;
import cn.atsoft.dasheng.uc.utils.MD5;
import cn.atsoft.dasheng.portal.banner.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.config.HttpConfig;
import lombok.SneakyThrows;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.HttpUtils;
import me.zhyd.oauth.utils.UrlBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShanyanRequest extends AuthDefaultRequest {

    private AuthUser authUser = new AuthUser();

    public ShanyanRequest(AuthConfig config) {
        super(config, ShanyanSource.SHANYAN_SOURCE);
    }

    public ShanyanRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, ShanyanSource.SHANYAN_SOURCE, authStateCache);
    }

    @SneakyThrows
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("token", authCallback.getCode());
        params.put("appId", config.getClientId());
//        params.put("encryptType", encryptType);//可以不传，不传则解密直接使用AES解密
        params.put("sign", SignUtils.getSign(params, config.getClientSecret()));
        String response = new HttpUtils(HttpConfig.builder().build()).post(accessTokenUrl(), params, false);
        JSONObject object = JSONObject.parseObject(response);
        if (null != object) {
            String code = object.getString("code");
            if ("200000".equals(code)) {
                String dataStr = object.getString("data");
                JSONObject dataObj = JSONObject.parseObject(dataStr);
                String mobile = dataObj.getString("mobileName");
                String key = MD5.getMD5Code(config.getClientSecret());
                mobile = AESUtils.decrypt(mobile, key.substring(0, 16), key.substring(16));
                this.authUser.setSource("SHANYAN");
                this.authUser.setUuid(mobile);
                this.authUser.setUsername(mobile);
                this.authUser.setNickname(mobile);
            }else{
                throw new ServiceException(500, code);
            }
        }else {
            throw new ServiceException(500, "response error");
        }
        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {

        return this.authUser;
    }

    public String accessTokenUrl() {
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .build();
    }

    /**
     * Description:签名工具类
     * User: liutao
     * Date: 2019-09-11
     * Time: 10:29
     */
    public static class SignUtils {

        public static String getSign(Map<String, String> requestMap, String appKey) {
            return hmacSHA256Encrypt(requestMap2Str(requestMap), appKey);
        }


        private static String hmacSHA256Encrypt(String encryptText, String encryptKey) {
            byte[] result = null;
            try {
                //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
                SecretKeySpec signinKey = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "HmacSHA256");
                //生成一个指定 Mac 算法 的 Mac 对象
                Mac mac = Mac.getInstance("HmacSHA256");
                //用给定密钥初始化 Mac 对象
                mac.init(signinKey);
                //完成 Mac 操作
                byte[] rawHmac = mac.doFinal(encryptText.getBytes("UTF-8"));
                return ByteFormat.bytesToHexString(rawHmac);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        private static String requestMap2Str(Map<String, String> requestMap) {
            String[] keys = requestMap.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : keys) {
                if (!str.equals("sign")) {
                    stringBuilder.append(str).append(requestMap.get(str));
                }
            }
            return stringBuilder.toString();
        }

    }

}

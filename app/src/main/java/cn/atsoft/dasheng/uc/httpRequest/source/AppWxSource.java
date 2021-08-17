package cn.atsoft.dasheng.uc.httpRequest.source;

import me.zhyd.oauth.config.AuthSource;

public enum AppWxSource implements AuthSource {
    APPWX {
        @Override
        public String authorize() {
            return null;
        }

        @Override
        public String accessToken() {
            return "https://api.weixin.qq.com/sns/oauth2/access_token";
        }

        @Override
        public String userInfo() {
            return null;
        }
    }
}

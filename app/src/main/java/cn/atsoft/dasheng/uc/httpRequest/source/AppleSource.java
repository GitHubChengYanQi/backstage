package cn.atsoft.dasheng.uc.httpRequest.source;

import me.zhyd.oauth.config.AuthSource;

public enum AppleSource implements AuthSource {
    APPLE_SOURCE {
        @Override
        public String authorize() {
            return null;
        }

        @Override
        public String accessToken() {
            return "https://appleid.apple.com/auth/keys";
        }

        @Override
        public String userInfo() {
            return "";
        }
    }
}

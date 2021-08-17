package cn.atsoft.dasheng.uc.httpRequest.source;

import me.zhyd.oauth.config.AuthSource;

public enum ShanyanSource implements AuthSource {
    SHANYAN_SOURCE {
        @Override
        public String authorize() {
            return null;
        }

        @Override
        public String accessToken() {
            return "https://api.253.com/open/flashsdk/mobile-query";
        }

        @Override
        public String userInfo() {
            return "";
        }
    }
}
